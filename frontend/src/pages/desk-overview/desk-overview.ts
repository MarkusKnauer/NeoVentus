import {Component} from "@angular/core";
import {ModalController, NavController} from "ionic-angular";
import {DeskService} from "../../app/service/desk.service";
import {AuthGuardService} from "../../app/service/auth-guard.service";
import {LoginPage} from "../login/login";
import {DeskPage} from "../desk/desk";

/**
 * @author Tim Heidelbach, Dennis Thanner
 * @version 0.0.4 adapted design to mockup
 *          0.0.3 button toggles between grid- and list view
 *          0.0.2 added authGuard - DT
 */
@Component({
  templateUrl: "desk-overview.html",
  providers: [DeskService]
})
export class DeskOverviewPage {

  public desks: any;
  private tileView = false; // must be false on startup to make footer-toolbar shrink ion-content
  private myDesksOnly = false;

  constructor(private navCtrl: NavController, private deskService: DeskService, private authGuard: AuthGuardService,
              public modalCtrl: ModalController) {
    this.loadDesks();
  }

  loadDesks() {
    this.deskService.getAllDesks()
      .then(data => {
        this.desks = data;

        // TODO get real data instead
        for (let desk of this.desks) {
          desk.price = 13.37;
          desk.waiter = "Knut Kessel";
          desk.reservation = "18:15 - 20:00  (Müller)";
          desk.status = 1;
        }
        this.desks[1].status = 2;
        this.desks[8].status = 0;
        this.desks[9].status = 0;
        this.desks[8].price = 0;
        this.desks[9].price = 0;
        this.desks[8].waiter = "";
        this.desks[9].waiter = "";
        this.desks[9].reservation = "";
      })
  }

  ionViewWillEnter() {
    if (!this.authGuard.hasAnyRole(["CEO", "SERVICE"])) {
      this.navCtrl.setRoot(LoginPage);
    }
  }

  deskSelected(desk) {
    let modal = this.modalCtrl.create(DeskPage, desk);
    modal.present();
  }

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
