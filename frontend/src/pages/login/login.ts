import {Component} from "@angular/core";
import {NavController, ToastController} from "ionic-angular";
import {UserService} from "../../app/service/user.service";
import {DeskOverviewPage} from "../desk-overview/desk-overview";
import {AuthGuardService} from "../../app/service/auth-guard.service";

/**
 * @author Dennis Thanner
 * @version 0.0.3 added login feedback + bug fix - DT
 * 0.0.2 added redirect to deskoverview
 */
@Component({
  selector: 'page-login',
  templateUrl: 'login.html'
})
export class LoginPage {

  private username: string;

  private password: string;

  constructor(public navCtrl: NavController, private userService: UserService, private authGuard: AuthGuardService,
              private toastCtrl: ToastController) {

  }

  /**
   * prevent login in screen to show up in browser if user is authenticated
   */
  ionViewWillEnter() {
    if (this.authGuard.isAuthenticated()) {
      this.navCtrl.setRoot(DeskOverviewPage)
    }
  }

  /**
   * login user
   * redirect to DeskOverviewPage if successful
   */
  public login() {
    this.userService.login(this.username, this.password).then(() => {
      // redirect to desk overview
      this.navCtrl.setRoot(DeskOverviewPage)
    }).catch(() => {
      console.debug("Failed to login");
      let infoToast = this.toastCtrl.create({
        message: "Invalid login",
        duration: 3000,
        position: "bottom"
      });
      infoToast.present();
    })
  }

}
