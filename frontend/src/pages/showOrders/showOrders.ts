/**
 * Created by julian on 03.05.2017.
 */
import {Component} from "@angular/core";
import {LoadingController, NavController, NavParams} from "ionic-angular";
import {ShowOrdersService} from "../../app/service/showOrders.service";
import {AuthGuardService} from "../../app/service/auth-guard.service";
import {DeskOverviewPage} from "../desk-overview/desk-overview";


/**
 * @author Julian beck
 * @version 0.0.2 "Name-value-pair"- compatibility
 *          0.0.1 created showorders.ts - JB
 */
@Component({
  templateUrl: "showOrders.html",
  providers: [ShowOrdersService]
})
export class ShowOrdersPage {

  public showOrders: any;
  public menuId: any;
  public deskNumber: any;
  public categoryString: string;


  constructor(public navParams: NavParams, private navCtrl: NavController, private showOrderService: ShowOrdersService, private authGuard: AuthGuardService,public loadingCtrl: LoadingController) {
    this.deskNumber = navParams.get("deskNumber");
    this.categoryString = "";
    if (this.deskNumber != null){
      this.loadOrders();
      this.presentLoadingDefault();
    } else{
      this.navCtrl.push(DeskOverviewPage);
    }
  }

  loadOrders() {
    this.showOrderService.listOrders("?deskNumber="+this.deskNumber.toString())
      .then(
       orderData => {
          this.showOrders = orderData;
        }
      );
    console.log("THIS IS FRONTEND - Received Order data"+ this.showOrders);
  }

  checkCategory(cat: string){
    console.log("THIS IS FRONTEND - Received Order cat"+ cat + " CategoryString: "+ this.categoryString);
    // if orderItem is a new Category
      if (cat !== this.categoryString){
        this.categoryString = cat;
        return true;
      } else {
        this.categoryString = cat;
        return false;
      }
  }


  toggleView() {
    // TODO: toggle between grid and list view
    alert("ein sehr mächtiger Button");
  }

// Fancy Loading circle
  presentLoadingDefault() {
    let loading = this.loadingCtrl.create({
      content: 'Bestellungen werden geladen  Das Tagesangebot ist: Shrimps mit Reis'
    });

    loading.present();

    setTimeout(() => {
      loading.dismiss();
    }, 5000);
  }



}
