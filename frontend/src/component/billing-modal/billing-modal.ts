import {Component} from "@angular/core";
import {Events, ModalController, NavParams, ViewController} from "ionic-angular";
import {OrderService} from "../../service/order.service";
import {BillingCheckoutModalComponent} from "../billing-checkout-modal/billing-checkout-modal";
/**
 * @author Dennis Thanner
 */
@Component({
  selector: "billing-modal",
  templateUrl: "billing-modal.html"
})
export class BillingModalComponent {

  private deskNumber: number;

  private cachedGroups;

  private selection = {};

  constructor(private navParams: NavParams, private viewCtrl: ViewController, private orderService: OrderService,
              private modalCtrl: ModalController, private events: Events) {
    this.deskNumber = navParams.get("deskNumber");

    // init component
    this.init();
  }

  /**
   * init modal content
   * @param force
   */
  private init(force?: boolean) {
    this.selection = {};
    return this.orderService.getOrdersByDeskNumber(this.deskNumber, force).then(() => {
      this.cachedGroups = this.orderService.cache["orders_desk" + this.deskNumber];
      // sort elements by name
      this.cachedGroups.sort((a, b) => {
        return a.item.name.localeCompare(b.item.name);
      });

      // publish event to update desk page
      if (force) {
        this.events.publish("order-change-" + this.deskNumber);
      }

      // set default selection to all
      for (let group of this.cachedGroups) {
        for (let id of group.orderIds) {
          this.selection[id] = true;
        }
      }
    });
  }

  /**
   * get total price of selection
   *
   * @returns {number}
   */
  public totalSelectedPrice() {
    let sum = 0;

    for (let key in this.selection) {

      if (this.selection[key]) {
        let group = this.cachedGroups.find(el => {
          return el.orderIds.indexOf(key) != -1;
        });

        sum += group.item.price;

        // add side dish prices
        for (let sd of group.sideDishes) {
          sum += sd.price;
        }
      }

    }

    return sum;
  }


  /**
   * open billing checkout modal
   */
  openBillingCheckout() {
    let data = {
      ids: [],
      sum: this.totalSelectedPrice()
    };
    for (let key in this.selection) {
      if (this.selection[key]) {
        data.ids.push(key);
      }
    }
    console.debug("Opening checkout modal with data", data);

    let checkoutModal = this.modalCtrl.create(BillingCheckoutModalComponent, data);
    checkoutModal.present();

    checkoutModal.onDidDismiss(success => {
      if (success) {
        // re initialize component and force reload data
        this.init(true).then(() => {

          if (this.cachedGroups.length == 0) {
            // all orders payed
            this.viewCtrl.dismiss(true);
          }
        })
      }
    })

  }


  /**
   * set complete selection to state
   * @param state
   */
  public setSelection(state: boolean) {
    for (let key in this.selection) {
      this.selection[key] = state;
    }
  }

  /**
   * close billing modal
   */
  public cancel() {
    this.viewCtrl.dismiss();
  }

}
