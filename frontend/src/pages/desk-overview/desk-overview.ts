import {Component, ViewChild} from "@angular/core";
import {Content, NavController} from "ionic-angular";
import {DeskService} from "../../service/desk.service";
import {AuthGuardService} from "../../service/auth-guard.service";
import {LoginPage} from "../login/login";
import {DeskPage} from "../desk/desk";
import {OrderService} from "../../service/order.service";

/**
 * @author Tim Heidelbach, Dennis Thanner
 * @version 0.0.8 toggle my desks
 *          0.0.7 added desk cache - DT
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
  private user = null;

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
        let strWaiters: string = "";
        let totalPrice: number = 0;

        for (let order of orders) {
          waiters.add(order.waiter.username); // set to ignore duplicate waiters
          totalPrice += order.item.price * order.count;
        }

        var waiterCount: number = 0;
        waiters.forEach((waiter: string) => {
          strWaiters += waiter;
          if (++waiterCount < waiters.size) {
            strWaiters += ", ";
          }
        });

        if (strWaiters != "") {
          desk.waiter = strWaiters
          this.deskService.cache['desks'].waiter = desk.waiter;
        }

        if (totalPrice != 0) {
          desk.price = totalPrice.toFixed(2); // some weird but negligible rounding errors happen here
          this.deskService.cache['desks'].price = desk.price;
          this.deskService.cache['desks'].status = 1;
        } else {
          desk.status = 0; // desk unused
          this.deskService.cache['desks'].status = 0;
        }

        if (waiters.has(this.getUserName())) {
          desk.mine = true;
          this.deskService.cache['desks'].mine = true;
        }
      }
      // TODO get reservation (status = 2, desk.reservation = 00:00)
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

  toggleMyDesksOnly = function () {

    if (this.myDesksOnly) {
      this.myDesksOnly = false;
      console.log("all desks");

    } else {
      console.log("my desks only");
      this.myDesksOnly = true;
    }
  }

  private getUserName() {
    if (this.user == null) {
      try {
        this.user = this.authGuard.userDetails.name;
      } catch (exception) {
        console.error("Desk overview - Cannot read username");
      }
    }
    return this.user;
  }

}
