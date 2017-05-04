import {Component} from "@angular/core";
import {ActionSheetController, NavController} from "ionic-angular";
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

  constructor(private navCtrl: NavController, private deskService: DeskService, private authGuard: AuthGuardService,
              public actionSheetCtrl: ActionSheetController) {
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

    let actionSheet = this.actionSheetCtrl.create({
      //title: 'Modify your album',
      buttons: [
        {
          text: 'Bestellung aufnehmen',
          role: 'destructive',
          handler: () => {
            console.log('Destructive clicked');
          }
        },{
          text: 'Bestellung ändern',
          handler: () => {
            console.log('Archive clicked');
          }
        },{
          text: 'Rechnung',
          role: 'cancel',
          handler: () => {
            console.log('Cancel clicked');
          }
        }
      ]
    });
    actionSheet.present();

  }

  toggleView() {
    // TODO: toggle between grid and list view
    // alert("ein sehr mächtiger Button");
    this.tileView ? this.tileView = false : this.tileView = true;
    console.log("tileView: " + this.tileView);
  }

}
