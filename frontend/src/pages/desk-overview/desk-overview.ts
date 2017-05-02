import {Component} from "@angular/core";
import {NavController} from "ionic-angular";
import {DeskService} from "../../app/service/desk.service";
import {AuthGuardService} from "../../app/service/auth-guard.service";
import {LoginPage} from "../login/login";

/**
 * @author Tim Heidelbach, Dennis Thanner
 * @version 0.0.3 button toggles between grid- and list view
 *          0.0.2 added authGuard - DT
 */
@Component({
  templateUrl: "desk-overview.html",
  providers: [DeskService]
})
export class DeskOverviewPage {

  public desks: any;
  private tileView = true;

  constructor(private navCtrl: NavController, private deskService: DeskService, private authGuard: AuthGuardService) {
    this.loadDesks();
  }

  loadDesks() {
    this.deskService.getAllDesks()
      .then(
        data => {
          this.desks = data;
        }
      )
  }

  ionViewWillEnter() {
    if (!this.authGuard.hasAnyRole(["CEO", "SERVICE"])) {
      this.navCtrl.setRoot(LoginPage);
    }
  }

  deskSelected(desk) {
    alert("you clicked desk " + desk.number);
  }

  toggleView() {
    // TODO: toggle between grid and list view
    // alert("ein sehr mächtiger Button");
    this.tileView ? this.tileView = false : this.tileView = true;
    console.log("tileView: " + this.tileView);
  }

}
