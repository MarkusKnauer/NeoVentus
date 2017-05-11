import {Component} from "@angular/core";
import {LoadingController, ModalController, NavController, NavParams} from "ionic-angular";
import {OrderService} from "../../service/order.service";
import {AuthGuardService} from "../../service/auth-guard.service";
import {DeskOverviewPage} from "../desk-overview/desk-overview";
import {OrderSelectModalComponent} from "../../component/order-select-modal/order-select-modal";
import {MenuCategoryService} from "../../service/menu-category.service";


/**
 * @author Julian beck, Dennis Thanner
 * @version 0.0.4 refactored grouped order ouput - DT
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

  constructor(public navParams: NavParams, private navCtrl: NavController, private orderService: OrderService,
              private authGuard: AuthGuardService, public loadingCtrl: LoadingController, private modalCtrl: ModalController,
              private menuCategoryService: MenuCategoryService) {
    this.deskNumber = navParams.get("deskNumber");
    if (this.deskNumber != null) {
      this.presentLoadingDefault();

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
    return this.getAggregatedOrders().filter(el => {
      return catIds.indexOf(el.item.menuItemCategory.id) != -1;
    });
  }

  /**
   * aggregate orders
   */
  getAggregatedOrders(): Array<any> {
    let result = [];
    let raw = this.orderService.cache["orders_desk" + this.deskNumber];
    for (let ord of raw) {
      // prevent duplicate adding
      if (result.find((el) => {
          return el.item.id == ord.item.id
        }) == null) {
        let tmp = raw.filter((order) => {
          return order.item.id == ord.item.id;
        });

        console.debug(tmp);
        tmp[0].count = tmp.length;
        tmp[0].price = 0;
        tmp.forEach(el => {
          tmp[0].price += el.item.price;
        });

        result.push(tmp[0])
      }
    }
    return result;
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
        sum += order.item.price;
      }
    }
    return sum;
  }

  /**
   * method to open order select modal
   */
  openOrderSelectModal() {
    let modal = this.modalCtrl.create(OrderSelectModalComponent);
    modal.present();
  }

// Fancy Loading circle
  presentLoadingDefault() {
    this.loading = this.loadingCtrl.create({
      content: 'Bestellungen werden geladen.'
    });

    this.loading.present();
  }


}
