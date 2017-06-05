import {Component} from "@angular/core";
import {LoadingController, NavController} from "ionic-angular";
import {AuthGuardService} from "../../service/auth-guard.service";
import {OrderService} from "../../service/order.service";
import {ReservationService} from "../../service/reservation.service";
import {DeskService} from "../../service/desk.service";
/**
 * @author Markus Knauer
 * @version 0.0.1
 */

@Component({
  selector: 'reservation-message',
  templateUrl: 'reservation.html'
})
export class ReservationPage {
  private loading;
  private now: Date;
  private desks: any;


  public event = {
    startDate: new Date().toISOString(),
    endDate: new Date().toISOString(),

  };

  private newDay = [];

  constructor(private authGuard: AuthGuardService,
              public loadingCtrl: LoadingController,
              public navCtrl: NavController,
              private orderService: OrderService,
              private reservationService: ReservationService,
              private deskService: DeskService) {

    this.now = new Date();

    this.deskService.getAllDesks().then(
      desks => {
        this.desks = desks;


      });
  }

  /**
   * Select free dishes
   * @returns {undefined}
   */
  getReservation() {
    console.debug("get Reservation");

    //tue etwas

  }


  compareDates() {
    let compare: any;
    let dat1 = Date.parse(this.event.startDate);
    let dat2 = Date.parse(this.event.endDate);
    compare = dat1 - dat2;
    if (compare == 0) {
      return 0;
    } else if (compare < 0) {
      return -1;
    } else if (compare > 0) {
      return 1;
    }
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
}
