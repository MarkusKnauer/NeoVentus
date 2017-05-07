import {Component, ViewChild} from "@angular/core";
import {Content, NavController} from "ionic-angular";
import {DeskService} from "../../app/service/desk.service";
import {AuthGuardService} from "../../app/service/auth-guard.service";
import {LoginPage} from "../login/login";
import {ShowOrdersPage} from "../showOrders/showOrders";
import {ShowOrdersService} from "../../app/service/showOrders.service";

/**
 * @author Tim Heidelbach, Dennis Thanner
 * @version 0.0.5 added waiter name
 *          0.0.4 adapted design to mockup
 *          0.0.3 button toggles between grid- and list view
 *          0.0.2 added authGuard - DT
 */
@Component({
  templateUrl: "desk-overview.html",
  providers: [DeskService, ShowOrdersService]
})
export class DeskOverviewPage {

  private desks: any;

  private tileView = true;
  private myDesksOnly = false;

  constructor(private navCtrl: NavController,
              private deskService: DeskService,
              private orderService: ShowOrdersService,
              private authGuard: AuthGuardService) {

    this.loadDesks();
  }

  loadDesks() {
    this.deskService.getAllDesks().then(
      desks => {
        this.desks = desks;
        for (let desk of this.desks) {
          this.loadDeskOrderDetails(desk);
        }
      }
    )
  }

  loadDeskOrderDetails(desk: any) {
    this.orderService.listOrderDesk(desk.number).then(
      order => {
        desk.waiter = order[0].waiter.username;
      }
    )
  }

  ionViewWillEnter() {
    if (!this.authGuard.hasAnyRole(["ROLE_CEO", "ROLE_SERVICE"])) {
      this.navCtrl.setRoot(LoginPage);
    }
  }

  deskSelected(desk) {
    this.navCtrl.push(ShowOrdersPage, {deskNumber: desk.number.toString()})
  }

  @ViewChild(Content) content: Content;
  toggleView() {
    this.tileView ? this.tileView = false : this.tileView = true;
    this.content.resize(); // lets ion-content respect footer height after grid/list toggle
  }

  toggleMyDesksOnly() {
    // TODO: exclude other waiters desk

    if (this.myDesksOnly) {
      this.myDesksOnly = false;
      console.log("all desks");

    } else {
      console.log("my desks only");
      this.myDesksOnly = true;
    }
  }
}
