import {Component} from "@angular/core";
import {LoadingController, ModalController, NavController, NavParams} from "ionic-angular";
import {OrderService} from "../../service/order.service";
import {AuthGuardService} from "../../service/auth-guard.service";
import {DeskOverviewPage} from "../desk-overview/desk-overview";
import {OrderSelectModalComponent} from "../../component/order-select-modal/order-select-modal";
import {MenuCategoryService} from "../../service/menu-category.service";
import {Order} from "../../model/order";
import {Utils} from "../../app/utils";
import {OrderDto} from "../../model/order-dto";


/**
 * @author Julian beck, Dennis Thanner
 * @version 0.0.7 batch request for order submitting -DT
 *          0.0.6 added submit orders - DT
 *          0.0.5 added tmp orders - DT
 *          0.0.4 refactored grouped order ouput - DT
 *          0.0.3 finished grouped order ouput and total - DT
 *          0.0.2 "Name-value-pair"- compatibility
 *          0.0.1 created showorders.ts - JB
 */
@Component({
  templateUrl: "desk.html",
})
export class DeskPage {

  public deskNumber: any;

  private loading;

  private catGroups = [];

  private tmpOrders: Array<Order> = [];

  private groupedTmpOrders = [];

  private ordersCacheKey: string;

  constructor(public navParams: NavParams, private navCtrl: NavController, private orderService: OrderService,
              private authGuard: AuthGuardService, public loadingCtrl: LoadingController, private modalCtrl: ModalController,
              private menuCategoryService: MenuCategoryService) {
    this.deskNumber = navParams.get("deskNumber");
    this.ordersCacheKey = "orders_desk" + this.deskNumber;
    if (this.deskNumber != null) {
      this.presentLoadingDefault('Bestellungen werden geladen.');

      Promise.all([
        this.menuCategoryService.loadCategoryTree(),
        this.orderService.getOrdersByDesk(this.deskNumber)
      ]).then(() => {
        this.loading.dismissAll();
        this.initCatGroups();
      })
    } else {
      this.navCtrl.push(DeskOverviewPage);
    }
  }

  /**
   * build root category groups
   * by getting all child ids
   */
  initCatGroups() {
    for (let cat of this.menuCategoryService.cache["tree"]) {
      let catIds = this.getChildCategoryIds(cat);
      catIds.push(cat.id);
      this.catGroups.push({name: cat.name, orders: this.getOrdersByCat(catIds)});
    }
    console.debug("Page Category Groups:", this.catGroups);
  }

  /**
   * traverse menu category tree to get a id array
   * @param cat
   */
  getChildCategoryIds(cat) {
    return cat.subcategory.map((child) => {
      return [child.id].join(this.getChildCategoryIds(child));
    });
  }

  /**
   * get orders by root category ids with child ids
   *
   * @param catIds
   */
  getOrdersByCat(catIds) {
    return this.orderService.cache[this.ordersCacheKey].filter(el => {
      return catIds.indexOf(el.item.menuItemCategory.id) != -1;
    });
  }


  /**
   * sum desk total order value
   *
   * @returns {number}
   */
  getTotalPrice(): number {
    let sum = 0;
    for (let cat of this.catGroups) {
      for (let order of cat.orders) {
        sum += order.item.price * order.count;
      }
    }
    for (let tmpOrder of this.tmpOrders) {
      sum += tmpOrder.item.price;
      for (let sd of tmpOrder.sideDishes) {
        sum += sd.price;
      }
    }
    return sum;
  }

  /**
   * method to open order select modal
   */
  openOrderSelectModal() {
    let modal = this.modalCtrl.create(OrderSelectModalComponent, {tmpOrders: this.tmpOrders});
    modal.present();
    modal.onDidDismiss(() => {
      this.getGroupedTmpOrders();
    })
  }

  /**
   * group tmp order by item and sidedish
   *
   * @returns {Array}
   */
  getGroupedTmpOrders() {
    let result = [];
    for (let tmp of this.tmpOrders) {
      let rTmp = result.find(el => {
        return tmp.item.id == el.item.id && Utils.arraysEqual(tmp.sideDishes, el.sideDishes);
      });
      if (rTmp) {
        rTmp.count += 1;
      } else {
        result.push({item: tmp.item, sideDishes: tmp.sideDishes, count: 1});
      }
    }
    console.debug("grouped tmp orders", result);
    this.groupedTmpOrders = result;
    return result;
  }


  /**
   * show loading popup
   *
   * @param info info message to display
   */
  presentLoadingDefault(info) {
    this.loading = this.loadingCtrl.create({
      content: info
    });

    this.loading.present();
  }

  /**
   * send orders to servers
   */
  submitOrders() {
    this.presentLoadingDefault("Bestellungen werden gesendet");

    let orders = [];

    // build dtos
    for (let order of this.tmpOrders) {
      // let orderDto =
      orders.push(new OrderDto(
        this.deskNumber,
        this.authGuard.userDetails.principal.workerId,
        order.wish,
        order.sideDishes.map((el) => {
          return el.id
        }),
        order.item.number
      ));
    }

    // batch insert orders
    this.orderService.insertOrders(orders).toPromise().then(() => {
      this.orderService.getOrdersByDesk(this.deskNumber).then(() => {
        this.initCatGroups();
        this.loading.dismissAll();
      });
      // reset tmp order
      this.groupedTmpOrders = [];
    });
  }


}
