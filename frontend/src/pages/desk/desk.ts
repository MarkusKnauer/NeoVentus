import {Component, NgZone} from "@angular/core";
import {
  ActionSheetController,
  AlertController,
  Events,
  LoadingController,
  ModalController,
  NavController,
  NavParams,
  Platform
} from "ionic-angular";
import {OrderService} from "../../service/order.service";
import {AuthGuardService} from "../../service/auth-guard.service";
import {DeskOverviewPage} from "../desk-overview/desk-overview";
import {OrderSelectModalComponent} from "../../component/order-select-modal/order-select-modal";
import {MenuCategoryService} from "../../service/menu-category.service";
import {Order} from "../../model/order";
import {Utils} from "../../app/utils";
import {OrderDto} from "../../model/order-dto";
import {LocalStorageService} from "../../service/local-storage.service";
import {BillingModalComponent} from "../../component/billing-modal/billing-modal";
import {OrderGroupDetailModalComponent} from "../../component/order-group-detail-modal/order-group-detail-modal";

/**
 * @author Julian beck, Dennis Thanner
 */
@Component({
  templateUrl: "desk.html",
  selector: "desk-page"
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
              private menuCategoryService: MenuCategoryService, private alertCtrl: AlertController,
              private actionSheetCtrl: ActionSheetController, private localStorageService: LocalStorageService,
              private events: Events) {
    this.localStorageService.loadStornoReasons();
    this.deskNumber = navParams.get("deskNumber");
    this.ordersCacheKey = "orders_desk" + this.deskNumber;
    if (this.deskNumber != null) {
      this.presentLoadingDefault('Bestellungen werden geladen.');



      Promise.all([
        this.menuCategoryService.loadCategoryTree(),
        this.orderService.getOrdersByDeskNumber(this.deskNumber)
      ]).then(() => {
        this.loading.dismissAll();
        this.initCatGroups();
      })
    } else {
      this.navCtrl.push(DeskOverviewPage);
    }

    // listen to changes in cache made by ceckout to re group elements
    this.events.subscribe("order-change-" + this.deskNumber, () => {
      console.debug("Order Change Event");
      this.initCatGroups();
    })
  }

  /**
   * build root category groups
   * by getting all child ids
   */
  initCatGroups() {
    // clear array to prevent duplicates
    this.catGroups = [];

    for (let cat of this.menuCategoryService.cache["tree"]) {
      let catIds = this.getChildCategoryIds(cat);
      catIds.push(cat.id);
      this.catGroups.push({name: cat.name, orders: this.getOrdersByCat(catIds)});
    }
    console.debug("Page Category Groups:", this.catGroups);
  }

  /**
   * open billing modal
   */
  openBillingModal() {
    let modal = this.modalCtrl.create(BillingModalComponent, {deskNumber: this.deskNumber});
    modal.present();
    modal.onDidDismiss((allPaid) => {
      if (allPaid) {
        // all paid go to desk overview
        this.navCtrl.pop();
      } else {
        this.initCatGroups();
      }
    });
  }

  /**
   * open processing detail modal
   * @param group
   */
  public openProcessingDetailModal(group) {
    let modal = this.modalCtrl.create(OrderGroupDetailModalComponent, {
      orderIds: group.orderIds,
      itemId: group.item.id
    });
    modal.present();
  }

  /**
   * add order from shortcuts
   *
   * @param item
   * @param guestWish
   * @param sideDishes
   */
  addTmpOrder(item, sideDishes, guestWish) {
    this.tmpOrders.push(new Order(item, sideDishes, guestWish));
    this.getGroupedTmpOrders();
  }

  /**
   *
   * @param item
   * @param sideDishes
   * @param guestWish
   * @return {number}
   */
  public getTmpOrderCount(item, sideDishes, guestWish) {
    return this.tmpOrders.filter((el) => {
      return el.item == item && Utils.arraysEqual(sideDishes, el.sideDishes) && el.wish == guestWish;
    }).length;
  }

  /**
   *
   * @param item
   * @param sideDishes
   * @param guestWish
   */
  removeTmpOrder(item, sideDishes, guestWish) {
    let rmOrder = this.tmpOrders.find((el) => {
      return el.item == item && Utils.arraysEqual(sideDishes, el.sideDishes) && el.wish == guestWish;
    });
    this.tmpOrders.splice(this.tmpOrders.indexOf(rmOrder), 1);
    // re group orders
    this.getGroupedTmpOrders();
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
    let orders = this.orderService.cache[this.ordersCacheKey].filter(el => {
      return catIds.indexOf(el.item.menuItemCategory.id) != -1;
    });
    this.sortOrderGroupsByName(orders);
    return orders;
  }

  /**
   * sort groups by item name
   * @param groups
   */
  private sortOrderGroupsByName(groups) {
    groups.sort((a, b) => {
      return a.item.name.localeCompare(b.item.name);
    })
  }

  /**
   * protection method to prevent closing view if orders are not submitted
   *
   * @returns {boolean}
   */
  ionViewCanLeave() {
    if (!this.tmpOrders.length) {
      // no open not submitted orders
      return true;
    } else {
      let alert = this.alertCtrl.create({
        title: "Offene Bestellungen verwerfen?",
        buttons: [
          {
            text: "Nein",
            handler: () => {
              alert.dismiss();
            }
          },
          {
            text: "Ja",
            handler: () => {
              alert.dismiss().then(() => {
                // delete not submitted orders to allow go back
                this.tmpOrders = [];
                // go back
                this.navCtrl.pop();
              });
            }
          }
        ],
        enableBackdropDismiss: false
      });
      alert.present();

      // prevent from going back
      // decision is prompted as alert
      return false;
    }
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
   * group tmp order by item and sidedish and guest wish
   *
   * @returns {Array}
   */
  getGroupedTmpOrders() {
    let result = [];
    for (let tmp of this.tmpOrders) {
      // find group element
      let rTmp = result.find(el => {
        return tmp.item.id == el.item.id && Utils.arraysEqual(tmp.sideDishes, el.sideDishes)
          && tmp.wish == el.guestWish;
      });
      if (rTmp) {
        // group element found increase counter
        rTmp.count += 1;
      } else {
        // group not found, create new group
        result.push({item: tmp.item, sideDishes: tmp.sideDishes, count: 1, guestWish: tmp.wish});
      }
    }
    this.sortOrderGroupsByName(result);
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
   * open storno popup to chose which order to abort
   * @param group
   * @param all
   */
  openStornoAction(group, all: boolean) {
    console.debug(group, all);
    let orderIds = [];
    if (all) {
      orderIds = group.orderIds;
    } else {
      orderIds.push(group.orderIds[0]);
    }

    // get clickable buttons
    let buttons = [];
    for (let reason of this.localStorageService.cache[LocalStorageService.STORNO_REASONS_KEY]) {
      buttons.push({
        text: reason,
        handler: () => {
          actionSheet.dismiss(reason);
          return false;
        }
      });
    }
    buttons.push({text: "Abbrechen", role: "cancel"});

    let actionSheet = this.actionSheetCtrl.create({
      title: "Stornierungsgrund",
      buttons: buttons
    });
    actionSheet.present();

    // after close reload data
    actionSheet.onDidDismiss((reason) => {
      // only execute if reason is set
      if (reason) {
        this.orderService.cancelOrders(orderIds, reason).toPromise().then(() => {
          console.debug("Reload data");
          this.orderService.getOrdersByDeskNumber(this.deskNumber, true).then(() => {
            this.initCatGroups();
          });
        });
      }
    })
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
      // reset tmp order
      this.groupedTmpOrders = [];
      this.tmpOrders = [];

      this.events.publish("order-change-" + this.deskNumber, this.deskNumber);

      // reload data
      this.orderService.getOrdersByDeskNumber(this.deskNumber, true).then(() => {
        this.initCatGroups();
        this.loading.dismissAll();
      });
    });
  }




}
