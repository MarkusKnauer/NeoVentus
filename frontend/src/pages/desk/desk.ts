import {Component} from "@angular/core";
import {LoadingController, ModalController, NavController, NavParams} from "ionic-angular";
import {OrderService} from "../../service/order.service";
import {AuthGuardService} from "../../service/auth-guard.service";
import {DeskOverviewPage} from "../desk-overview/desk-overview";
import {OrderSelectModalComponent} from "../../component/order-select-modal/order-select-modal";


/**
 * @author Julian beck
 * @version 0.0.2 "Name-value-pair"- compatibility
 *          0.0.1 created showorders.ts - JB
 */

@Component({
  templateUrl: "desk.html",
})
export class DeskPage {

  public showOrders: any;
  public menuId: any;
  public deskNumber: any;
  public categoryString: string;


  constructor(public navParams: NavParams, private navCtrl: NavController, private showOrderService: OrderService,
              private authGuard: AuthGuardService, public loadingCtrl: LoadingController, private modalCtrl: ModalController) {
    this.deskNumber = navParams.get("deskNumber");
    this.categoryString = "";
    if (this.deskNumber != null){
      this.loadOrders();
      // this.presentLoadingDefault();
    } else{
      this.navCtrl.push(DeskOverviewPage);
    }
  }

  loadOrders() {
    this.showOrderService.getOrdersByDesk(this.deskNumber).then(
      orderData => {
        this.showOrders = orderData;
        console.log("ShowOrdersPage - Received Order data" + this.showOrders);
      }
    );
  }

  checkCategory(cat: string){
    // if orderItem is a new Category
      if (cat !== this.categoryString){
        this.categoryString = cat;
        return true;
      } else {
        this.categoryString = cat;
        return false;
      }
  }

  openOrderSelectModal() {
    let modal = this.modalCtrl.create(OrderSelectModalComponent);
    modal.present();
  }

// Fancy Loading circle
  presentLoadingDefault() {
    let loading = this.loadingCtrl.create({
      content: 'Bestellungen werden geladen.'
    });

    loading.present();

    setTimeout(() => {
      loading.dismiss();
    }, 5000);
  }



}
