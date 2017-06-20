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
  private selecteddesks: any;
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
    this.selecteddesks = null;

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

    let currTime = new Date(this.time).getTime();

    this.deskService.getNotReservedDesks(currTime).then(
      desks => {
        this.desks = desks;
      });

    //this.desks = this.deskService.cache["notReservedDesks"];

    /*
    this.reservationService.getReservationsByDesk(desk).then(
      reservations => {

        let next = [];
     let showdesk = false;

        let endtime = new Date();
        let pretime = new Date();
        let today = new Date();
        let curtime = new Date(this.time);

        if (reservations.length > 0) {
          showdesk = false;
        }

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
                next.push(reservationTime.toISOString());
              }
            }
          }


        }

        if (next != null) { // desk is reserved in requested time
          desk.reservationText = next;

        }
        if (showdesk) this.desks.push(desk);
      }
    )
     */
    this.suggestDesks()
  }

  suggestDesks() {

    let currSeats = 0;
    let i = 0;
    if (this.desks != null) {
      while (currSeats < this.guestnumber && i < this.desks.length) {
        this.selecteddesks.push(this.desks[i])
      }
    }

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
            console.debug(data);
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
      this.reservationDto.Desk = desk.id;
      this.reservationDto.Time = new Date(this.time);
      this.reservationDto.ReservedBy = this.getUserName();
      this.reservationDto.ReservationName = this.reservationName;
      console.debug(this.reservationDto);

      this.reservationService.insertReservation(this.reservationDto).toPromise().then((resp) => {
        this.reportUser(desk, this.reservationDto.ReservationName, this.reservationDto.Time);
        console.debug(resp);
      });
    }
  }

  reportUser(desk: any, reservationName: any, reservationTime: any) {

    let alert = this.alertCtrl.create({
      title: "Reservierung erfolgreich ",
      message: "Name: " + reservationName +
      " Datum: " + reservationTime.getDay() + "." + reservationTime.getMonth() + "." + reservationTime.getFullYear() +
      +" um " + reservationTime.getHours() + ":" + reservationTime.getMinutes() +
      " Tisch " + desk.number,
      buttons: [
        {
          text: "OK",
          handler: data => {
            console.debug(data);
          }
        }
      ],
      enableBackdropDismiss: false
    });
    alert.present();
  }

}
