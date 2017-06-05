import {Component} from "@angular/core";
import {LoadingController, NavController} from "ionic-angular";
import {AuthGuardService} from "../../service/auth-guard.service";
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

  public event = {
    startDate: new Date().toISOString(),
    endDate: new Date().toISOString(),

  };

  private newDay = [];

  constructor(private authGuard: AuthGuardService, public loadingCtrl: LoadingController, public navCtrl: NavController) {


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

}
