import {Component, NgZone} from "@angular/core";
import {Events, NavController, Platform} from "ionic-angular";
import {DeskService} from "../../service/desk.service";
import {AuthGuardService} from "../../service/auth-guard.service";
import {LoginPage} from "../login/login";
import {DeskPage} from "../desk/desk";
import {OrderService} from "../../service/order.service";
import {ReservationService} from "../../service/reservation.service";
import {BeaconService} from "../../service/beacon.service";
import {BeaconModel} from "../../model/beacon-module";

/**
 * @author Tim Heidelbach, Dennis Thanner
 */
@Component({
  templateUrl: "desk-overview.html",
})
export class DeskOverviewPage {

  private tileView = true;
  private myDesksOnly = false;
  private user = null;
  private desks: any;
  private now: Date;
  beacons: BeaconModel[] = [];
  zone: any;
  constructor(private navCtrl: NavController,
              private deskService: DeskService,
              private orderService: OrderService,
              private authGuard: AuthGuardService,
              private reservationService: ReservationService, private events: Events,
              public platform: Platform, public beaconService: BeaconService) {

    this.now = new Date();

    // required for UI update
    this.zone = new NgZone({ enableLongStackTrace: false });



    this.deskService.getAllDesks().then(
      desks => {
        this.desks = desks;

        // listen to billing changes and reload desk data
        this.events.subscribe("order-change", (number) => {
          console.debug("reload desk overview data for desk after billing");
          let desk = this.desks.find(el => {
            return el.number == number;
          });
          if (desk)
            this.loadDeskOrderDetails(desk, true);
        });

        for (let desk of desks) {

          this.loadDeskOrderDetails(desk);
          this.loadDeskReservationDetails(desk);
        }
      });
  }

  loadDeskOrderDetails(desk: any, force?) {
    this.orderService.getOrdersByDeskNumber(desk.number, force).then(
      orders => {

        let waiters = new Set<string>();
        let strWaiters: string = "";
        let totalPrice: number = 0;

        for (let order of orders) {
          waiters.add(order.waiter.username); // set to ignore duplicate waiters
          totalPrice += order.item.price * order.count;
        }

        let waiterCount: number = 0;
        waiters.forEach((waiter: string) => {
          strWaiters += waiter;
          if (++waiterCount < waiters.size) {
            strWaiters += ", ";
          }
        });


        desk.waiter = strWaiters;


        if (totalPrice != 0) {
          desk.inUse = true;
        } else {
          desk.inUse = false;
        }
        desk.price = totalPrice.toFixed(2);

        if (waiters.has(this.getUserName())) {
          desk.mine = true;
        }
      }
    )
  }

  loadDeskReservationDetails(desk: any) {
    this.reservationService.getReservationsByDesk(desk).then(
      reservations => {

        let next = null;

        for (let reservation of reservations) {

          let reservationTime = new Date(reservation.time);

          if (reservationTime > this.now) { // reservation is in the future

            if (next == null) {
              next = reservationTime;

            } else if (reservationTime < next) { // reservation is before previous reservation
              next = reservationTime;
            }
          }
        }

        if (next != null && next.getDay() == this.now.getDay()) { // desk is reserved today
          desk.reservation = next;
        }
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
  };

  private getUserName() {
    if (this.user == null) {
      try {
        this.user = this.authGuard.userDetails.name;
      } catch (exception) {
      }
    }
    return this.user;
  }


  ionViewDidLoad() {
    this.platform.ready().then(() => {
      this.beaconService.initialise().then((isInitialised) => {
        if (isInitialised) {
          this.listenToBeaconEvents();
        }
      });
    });
  }

  listenToBeaconEvents() {
    this.events.subscribe('didRangeBeaconsInRegion', (beaconData) => {

// update the UI with the beacon list
      this.zone.run(() => {

        this.beacons = [];

        let beaconList = beaconData.beacons;

        beaconList.forEach((beacon) => {

          let beaconObject = new BeaconModel(beacon);

          this.beacons.push(beaconObject);
        });

      });

    });
  }
}
