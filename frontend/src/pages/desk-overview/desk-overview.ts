import {Component, ViewChild} from "@angular/core";
import {Content, NavController} from "ionic-angular";
import {DeskService} from "../../service/desk.service";
import {AuthGuardService} from "../../service/auth-guard.service";
import {LoginPage} from "../login/login";
import {DeskPage} from "../desk/desk";
import {OrderService} from "../../service/order.service";

/**
 * @author Tim Heidelbach, Dennis Thanner
 * @version 0.0.7 added desk cache - DT
 *          0.0.6 rbma changes - DT
 *          0.0.5 added waiter name
 *          0.0.4 adapted design to mockup
 *          0.0.3 button toggles between grid- and list view
 *          0.0.2 added authGuard - DT
 */
@Component({
  templateUrl: "desk-overview.html",
})
export class DeskOverviewPage {

  private tileView = true;
  private myDesksOnly = false;
  // private desks: any;

  constructor(private navCtrl: NavController,
              private deskService: DeskService,
              private orderService: OrderService,
              private authGuard: AuthGuardService) {


    this.deskService.getAllDesks().then(
      desks => {
        for (let desk of desks) {
          this.loadDeskOrderDetails(desk);
        }
      });
  }


  loadDeskOrderDetails(desk: any) {
    this.orderService.getOrdersByDesk(desk.number).then(
      orders => {

        let waiters = new Set<string>();
        let strwaiters: string = "";

        for (let order of orders) {
          waiters.add(order.waiter);
        }

        for (let waiter of Array.from(waiters.values())) {
          strwaiters += waiter + ", ";
        }
        desk.waiter = strwaiters.substring(0, strwaiters.length - 2);
        this.deskService.cache['desks'].waiter = desk.waiter;

        // TODO get price sum
      }
    )
  }

  /**
   * view life cycle method
   *
   * RBMA
   */
  ionViewWillEnter() {
    this.authGuard.hasAnyRolePromise(["ROLE_CEO", "ROLE_SERVICE"]).then(() => {
    }, () => {
      console.debug("RBMA - Access denied!");
      this.navCtrl.setRoot(LoginPage);
    });
  }

  deskSelected(desk) {
    this.navCtrl.push(DeskPage, {deskNumber: desk.number.toString()})
  }

  @ViewChild(Content) content: Content;

  toggleView() {
    this.tileView ? this.tileView = false : this.tileView = true;
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
