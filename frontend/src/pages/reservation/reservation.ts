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
  selector: 'reservation',
  templateUrl: 'reservation.html'
})
export class ReservationPage {
  private loading;
  private time: Date;
  private desks: any;
  private alldesks: any;
  private guestnumber: number;

  /*
  public event = {
   Date: new Date().toISOString(),
  };
   */

  private newDay = [];

  constructor(private authGuard: AuthGuardService,
              public loadingCtrl: LoadingController,
              public navCtrl: NavController,
              private orderService: OrderService,
              private reservationService: ReservationService,
              private deskService: DeskService) {

    this.time = new Date();
    this.desks = [];

    this.deskService.getAllDesks().then(
      alldesks => {
        this.alldesks = alldesks;
      });


  }

  /**
   * Select free dishes
   * @returns {undefined}
   */
  getReservation() {
    console.debug("get Reservation");

    if (this.guestnumber <= 0 || this.guestnumber > 103) {

      alert("Bitte beachten Sie das mindestens einer und maximal 103 Personen auswgewählt werden können!")

    } else {

      if (this.alldesks != null) {
        if (this.desks.length == 0) {
          for (let desk of this.alldesks) {

            this.loadDeskAvailability(desk);

          }

        }
      }
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


  setTime(time: any) {
    this.time = time;
  }

  loadDeskAvailability(desk: any) {
    this.reservationService.getReservationsByDesk(desk).then(
      reservations => {

        let next = null;

        let endtime = new Date();
        let pretime = new Date();

        for (let reservation of reservations) {

          let reservationTime = new Date(reservation.time);

          endtime.setTime(reservationTime.getTime() + 3600000) //give one hour time to eat

          if (this.time > endtime) { // reservation is in the future, ok
            next = 1;
          }

          pretime.setTime(reservationTime.getTime() - 3600000)//give one hour time to eat
          if (this.time < pretime) {
            next = 1;
          }
        }
        if (next != null) this.desks.push(desk);

      }
    )
  }

  loadDeskReservationDetails(desk: any) {
    this.reservationService.getReservationsByDesk(desk).then(
      reservations => {

        let next = null;

        for (let reservation of reservations) {

          let reservationTime = new Date(reservation.time);

          if (reservationTime > this.time) { // reservation is in the future

            if (next == null) {
              next = reservationTime;

            } else if (reservationTime < next) { // reservation is before previous reservation
              next = reservationTime;
            }
          }
        }

        if (next != null && next.getDay() == this.time.getDay()) { // desk is reserved today
          desk.reservation = next;
        }
      }
    )
  }
}
