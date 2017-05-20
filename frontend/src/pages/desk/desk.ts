import {Component} from "@angular/core";
import {AlertController, LoadingController, ModalController, NavController, NavParams} from "ionic-angular";
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
              private menuCategoryService: MenuCategoryService, private alertCtrl: AlertController) {
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
   */
  openStornoPopup(group) {
    let data = [];
    let reqs = [];
    for (let orderId of group.orderIds) {
      let req = this.orderService.getOrderInfo(orderId);
      reqs.push(req);
      req.then((resp) => {
        data.push(resp.json());
      }, () => {
      })
    }

    // get data and then filter for cancel orders
    Promise.all(reqs).then(() => {
      let inputs = [];
      for (let d of data) {
        if (d.currentState == "NEW") {
          let date = new Date(d.states[0].date);
          inputs.push({
            type: "checkbox",
            label: d.item.shortName + " von " + d.waiter.username + " um " + date.getHours() + ":" + date.getMinutes(),
            value: d.id
          })
        }
      }

      // build alert
      let alert = this.alertCtrl.create({
        title: "Bestellungen stornieren?",
        message: !inputs.length ? "Keine Bestellung zum Stornieren verfügbar" : "",
        inputs: inputs,
        buttons: [
          {
            text: "Abbrechen",
            role: "cancel",
          },
          {
            text: "Ok"
          }
        ]
      });

      alert.present();

      // after confirmation execute action and reload
      alert.onDidDismiss((orderIds) => {
        if (orderIds.length) {
          this.orderService.cancleOrders(orderIds).toPromise().then(() => {
            // reload data
            this.orderService.getOrdersByDesk(this.deskNumber).then(() => {
              this.initCatGroups();
            });
          });
        }
      });
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

      // reload data
      this.orderService.getOrdersByDeskNumber(this.deskNumber).then(() => {
        this.initCatGroups();
        this.loading.dismissAll();
      });
    });
  }


}
