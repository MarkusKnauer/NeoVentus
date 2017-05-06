/**
 * Created by julian on 03.05.2017.
 */
import {Component} from "@angular/core";
import {NavController, NavParams} from "ionic-angular";
import {ShowOrdersService} from "../../app/service/showOrders.service";
import {AuthGuardService} from "../../app/service/auth-guard.service";
import {DeskOverviewPage} from "../desk-overview/desk-overview";


/**
 * @author Julian beck
 * @version 0.0.1 created showorders.ts - JB
 */
@Component({
  templateUrl: "showOrders.html",
  providers: [ShowOrdersService]
})
export class ShowOrdersPage {

  public showOrders: any;
  public deskNumber: any;
  public categoryString: string;
  public menuItemCounter: number;

  constructor(public navParams: NavParams, private navCtrl: NavController, private showOrderService: ShowOrdersService, private authGuard: AuthGuardService) {
    this.deskNumber = navParams.get("deskNumber");
    this.categoryString = "";
    if (this.deskNumber != null){
      this.loadOrders();
    } else{
      this.navCtrl.push(DeskOverviewPage);
    }
  }

  loadOrders() {
    this.showOrderService.listOrderDesk(this.deskNumber)
      .then(
       orderData => {
          this.showOrders = orderData;
        }
      );
    console.log("THIS IS FRONTEND - Received Order data"+ this.showOrders)
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
  getFullAmountDesk(){


  }



  toggleView() {
    // TODO: toggle between grid and list view
    alert("ein sehr mächtiger Button");
  }

}
