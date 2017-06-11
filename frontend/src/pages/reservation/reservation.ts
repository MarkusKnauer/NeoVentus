import {Component} from "@angular/core";
import {AlertController, LoadingController, NavController} from "ionic-angular";
import {AuthGuardService} from "../../service/auth-guard.service";
import {OrderService} from "../../service/order.service";
import {ReservationService} from "../../service/reservation.service";
import {DeskService} from "../../service/desk.service";
import {ReservationDto} from "../../model/reservation-dto";
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
  private username = null;
  private time: string;
  private temp: Date;
  private isSelected: boolean;
  private desks: any;
  private alldesks: any;
  private guestnumber: number;
  private reservationName: string;
  private reservationDto: ReservationDto;

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
              private deskService: DeskService,
              private alertCtrl: AlertController) {

    this.temp = new Date();
    this.time = new Date(this.temp.getTime() + 7200000).toISOString();
    this.desks = [];
    this.isSelected = false;
    this.guestnumber = 0;
    this.reservationName = null;

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
    this.isSelected = true;
    this.desks = [];
    if (this.guestnumber < 0 || this.guestnumber > 102) {

      alert("Im Restaurant haben max. 102 Personen platz!")

    } else {

      if (this.alldesks != null) {
        if (this.desks.length == 0) {
          if (this.guestnumber != 0) {
            for (let desk of this.alldesks) {

              this.loadDeskAvailability(desk);

            }
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

  getUserName() {
    if (this.username == null) {
      try {
        this.username = this.authGuard.userDetails.name;
      } catch (exception) {
        console.error("Profile - Cannot read username");
      }
    }
    return this.username;
  }

  loadDeskAvailability(desk: any) {
    this.reservationService.getReservationsByDesk(desk).then(
      reservations => {

        let next = null;
        let showdesk = false;

        let endtime = new Date();
        let pretime = new Date();
        let today = new Date();
        let curtime = new Date(this.time);

        for (let reservation of reservations) {

          let reservationTime = new Date(reservation.time);

          endtime.setTime(reservationTime.getTime() + 3600000) //give one hour time to eat

          if (curtime > endtime) { // reservation is in the future, ok
            showdesk = true;
          }

          pretime.setTime(reservationTime.getTime() - 3600000)//give one hour time to eat
          if (curtime < pretime) {
            showdesk = true;
          }

          if (reservationTime.getFullYear() == curtime.getFullYear()) {
            if (reservationTime.getMonth() == curtime.getMonth()) {
              if (reservationTime.getDay() == curtime.getDay()) {
                next += reservationTime;
              }
            }
          }
        }
        if (next != null) { // desk is reserved in requested time
          desk.reservation = next;
        }
        if (showdesk) this.desks.push(desk);
      }
    )
  }

  deskSelected(desk: any) {


    let alert = this.alertCtrl.create({
      title: "Reservierung Tisch: " + desk.number,
      inputs: [
        {
          name: 'gastname',
          placeholder: 'Name des Gastes'
        },
      ],
      buttons: [
        {
          text: "Abbrechen",
          role: 'cancel',
          handler: () => {
            console.log('Cancel clicked');
          }
        },
        {
          text: "Reservieren",
          handler: data => {
            this.reservationName = data.gastname;
            this.insertReservation(desk);
          }
        }
      ],
      enableBackdropDismiss: false
    });
    alert.present();
  }

  insertReservation(desk: any) {

    if (this.reservationName != null) {

      this.reservationDto = new ReservationDto();
      this.reservationDto.Desk = desk;
      this.reservationDto.Time = new Date(this.time);
      this.reservationDto.ReservedBy = this.getUserName();
      this.reservationDto.ReservationName = this.reservationName;

      this.reservationService.insertReservation(this.reservationDto);
    }
  }

}
