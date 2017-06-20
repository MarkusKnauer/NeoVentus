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
  private selecteddesks: any;
  private guestnumber: number;
  private reservationName: string;
  private reservationDto: ReservationDto;


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
    this.selecteddesks = [];


  }

  /**
   * Select free dishes
   * @returns {undefined}
   */
  getReservation() {

    let currTime = new Date(this.time).getTime() - 7200000;

    console.debug("get Reservation");
    this.desks = [];
    if (this.guestnumber < 0 || this.guestnumber > 102) {

      alert("Im Restaurant haben max. 102 Personen platz!")

    } else {

      if (this.desks.length == 0) {
          if (this.guestnumber != 0) {

            this.deskService.getNotReservedDesks(currTime).then(
              desks => {
                this.desks = desks;
                this.suggestDesks();
              });

            for (let desk of this.desks) {

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

    //todo load Reservation Time (MK)

  }

  suggestDesks() {

    this.selecteddesks = [];
    let currSeats = 0;
    let i = 0;
    if (this.desks != null) {

      for (let desk of this.desks) {
        if (this.guestnumber == desk.maximalSeats) {
          this.selecteddesks.push(this.desks[i]);
          this.isSelected = true;
          return;
        }
        i++;

      }

      i = 0;

      while (currSeats < this.guestnumber && i < this.desks.length) {

        this.selecteddesks.push(this.desks[i])
        currSeats += this.desks[i].maximalSeats;
        i++;
      }
      this.isSelected = true;
    }

  }

  chanceDesk(desk: any) {
    this.selecteddesks = [];
    this.selecteddesks.push(desk);

  }

  deskSelected() {
    let desk = this.selecteddesks[0];

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

      let temp = new Date(this.time);

      this.reservationDto = new ReservationDto();
      this.reservationDto.Desk = desk.id;
      this.reservationDto.Time = new Date(temp.getTime() - 7200000);
      this.reservationDto.ReservedBy = this.getUserName();
      this.reservationDto.ReservationName = this.reservationName;
      console.debug(this.reservationDto);

      this.reservationService.insertReservation(this.reservationDto).toPromise().then((resp) => {
        this.reportUser(desk, this.reservationDto.ReservationName, this.reservationDto.Time);
        console.debug(resp);
      });
    }

    this.desks = [];
    this.isSelected = false;
    this.guestnumber = 0;
    this.reservationName = null;
    this.selecteddesks = [];

  }

  reportUser(desk: any, reservationName: any, reservationTime: any) {

    let alert = this.alertCtrl.create({
      title: "Reservierung erfolgreich ",
      /* message: "Name: " + reservationName +
      " Datum: " + reservationTime.getDay() + "." + reservationTime.getMonth() + "." + reservationTime.getFullYear() +
      +" um " + reservationTime.getHours() + ":" + reservationTime.getMinutes() +
       " Tisch " + desk.number,*/
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
