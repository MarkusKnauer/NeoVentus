import {Component} from "@angular/core";
import {AlertController, LoadingController, NavController, ToastController} from "ionic-angular";
import {AuthGuardService} from "../../service/auth-guard.service";
import {ReservationService} from "../../service/reservation.service";
import {DeskService} from "../../service/desk.service";
import {ReservationDto} from "../../model/reservation-dto";
import {LoginPage} from "../login/login";
import {Role} from "../../app/roles";


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
  private notenoughSeats: boolean;



  constructor(private authGuard: AuthGuardService,
              public loadingCtrl: LoadingController,
              public navCtrl: NavController,
              private reservationService: ReservationService,
              private deskService: DeskService,
              private alertCtrl: AlertController,
              private toastCtrl: ToastController) {


    this.temp = new Date(new Date().getTime() + 7200000);

    // round time to next quarter hour
    let quarterHours = Math.ceil(this.temp.getMinutes() / 15);
    if (quarterHours == 4) {
      this.temp.setHours(this.temp.getHours() + 1);
    }
    let rounded = (quarterHours * 15) % 60;
    this.temp.setMinutes(rounded);

    this.time = new Date(this.temp.getTime()).toISOString();

    this.desks = [];
    this.isSelected = false;
    this.guestnumber = 0;
    this.reservationName = null;
    this.selecteddesks = [];
    this.notenoughSeats = false;


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

        if (this.guestnumber != 0) {

          this.deskService.getNotReservedDesks(currTime).then(
            desks => {
              this.desks = desks;
              this.suggestDesks();
            });

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

        this.selecteddesks.push(this.desks[i]);
        currSeats += this.desks[i].maximalSeats;
        i++;
      }
      this.isSelected = true;
    }

  }

  changeDesk(desk: any) {

    let currSeats = 0;

    if (!this.notenoughSeats) {

      //this.notenoughSeats = false;
      this.selecteddesks = [];

      console.debug(this.selecteddesks);
      let deskExist = this.selecteddesks.find(el => {
        return el.id == desk.id;
      });
      if (deskExist == null)
        this.selecteddesks.push(desk);

      //let currSeats = desk.maximalSeats;
      for (let d of this.selecteddesks) {
        currSeats += d.maximalSeats;
      }
      if (currSeats < this.guestnumber) {
        this.notenoughSeats = true;
      }

    } else if (this.notenoughSeats) { //Add Desks until Seats reaches level of guests

      let deskExist = this.selecteddesks.find(el => {
        return el.id == desk.id;
      });
      if (deskExist == null)
        this.selecteddesks.push(desk);

      for (let d of this.selecteddesks) {
        currSeats += d.maximalSeats;
      }
      if (currSeats >= this.guestnumber) {
        this.notenoughSeats = false;
      }


    }
  }

  deskSelected() {


    let alert = this.alertCtrl.create({
      title: "Reservierung Tisch: " + this.selecteddesks.map(el => {
        return el.number;
      }).join(", "),
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
            this.insertReservation();
          }
        }
      ],
      enableBackdropDismiss: false
    });
    alert.present();
  }

  insertReservation() {

    if (this.reservationName != null) {

      let temp = new Date(this.time);

      this.reservationDto = new ReservationDto();
      this.reservationDto.Desk = this.selecteddesks.map(el => {
        return el.id
      });
      this.reservationDto.Time = new Date(temp.getTime() - 7200000);
      this.reservationDto.ReservedBy = this.getUserName();
      this.reservationDto.ReservationName = this.reservationName;

      this.reservationService.insertReservation(this.reservationDto).toPromise().then((resp) => {
        this.reportUser();
      });
    }

    this.desks = [];
    this.isSelected = false;
    this.guestnumber = 0;
    this.reservationName = null;
    this.selecteddesks = [];
    this.guestnumber = 0;

  }

  /**
   * view life cycle method
   *
   * RBMA
   */
  ionViewWillEnter() {
    this.authGuard.hasAnyRolePromise([Role.SERVICE, Role.BAR, Role.CEO]).then(() => {
    }, () => {
      console.debug("RBMA - Access denied!");
      this.navCtrl.setRoot(LoginPage);
    });
  }

  reportUser() {
    let alert = this.toastCtrl.create({
      message: "Reservierung erfolgreich hinzugefügt",
      duration: 3000
    });
    alert.present();
  }

}
