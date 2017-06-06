import {Component} from "@angular/core";
import {NavController, ToastController} from "ionic-angular";
import {UserService} from "../../service/user.service";
import {DeskOverviewPage} from "../desk-overview/desk-overview";
import {AuthGuardService} from "../../service/auth-guard.service";
import {KitchenOverviewPage} from "../kitchen-overview/kitchen-overview";

/**
 * @author Dennis Thanner
 * @version 0.0.4 rbma changes - DT
 *          0.0.3 added login feedback + bug fix - DT
 *          0.0.2 added redirect to deskoverview - DT
 */
@Component({
  selector: 'page-login',
  templateUrl: 'login.html'
})
export class LoginPage {

  private username: string = "";

  private password: string = "";

  constructor(public navCtrl: NavController, private userService: UserService, private authGuard: AuthGuardService,
              private toastCtrl: ToastController) {

  }

  /**
   * prevent login in screen to show up in browser if user is authenticated
   */
  ionViewWillEnter() {
    this.redirectUser();
  }

  /**
   * login user
   * redirect to DeskOverviewPage if successful
   */
  public login() {
    this.userService.login(this.username, this.password).then(() => {
      // redirect to desk overview
      this.redirectUser();
    }).catch(() => {
      console.debug("Failed to login");
      let infoToast = this.toastCtrl.create({
        message: "Falsche Kombination aus Benutzername und Passwort",
        duration: 3000,
        position: "bottom"
      });
      infoToast.present();
    })
  }

  /**
   * redirect user to specific pages after login
   */
  private redirectUser() {
    this.authGuard.isAuthenticatedPromise().then(() => {
      console.debug("User already authenticated");
      if (this.authGuard.hasRole("ROLE_CHEF")) {
        this.navCtrl.setRoot(KitchenOverviewPage, {forKitchen: 1})
      } else if (this.authGuard.hasRole("ROLE_BAR")) {
        this.navCtrl.setRoot(KitchenOverviewPage, {forKitchen: 0})
      } else {
        this.navCtrl.setRoot(DeskOverviewPage)
      }
    }, () => {
    });

  }

}
