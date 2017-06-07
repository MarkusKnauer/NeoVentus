import {Component} from "@angular/core";
import {NavController, Platform, ToastController} from "ionic-angular";
import {FingerprintAIO} from "@ionic-native/fingerprint-aio";
import {SecureStorage, SecureStorageObject} from "@ionic-native/secure-storage";
import {UserService} from "../../service/user.service";
import {DeskOverviewPage} from "../desk-overview/desk-overview";
import {AuthGuardService} from "../../service/auth-guard.service";
import {KitchenOverviewPage} from "../kitchen-overview/kitchen-overview";

/**
 * @author Dennis Thanner
 */
@Component({
  selector: 'page-login',
  templateUrl: 'login.html'
})
export class LoginPage {

  private static FAIO_SECRET = "neovent.us";
  private static STORAGE_NAME = "neovent.us";
  private static STORAGE_PREFIX = "secret_";

  private username: string = "";

  private password: string = "";

  private saveLogin: false;

  constructor(public navCtrl: NavController, private userService: UserService, private authGuard: AuthGuardService,
              private toastCtrl: ToastController, private faio: FingerprintAIO, private secureStorage: SecureStorage,
              private platform: Platform) {
    // on cordova look if username and password is saved in secure storage
    if (platform.is("cordova")) {
      this.secureStorage.create(LoginPage.STORAGE_NAME).then((storage: SecureStorageObject) => {

        storage.get(LoginPage.STORAGE_PREFIX + "username").then((data) => {
          this.username = data;
        });
        storage.get(LoginPage.STORAGE_PREFIX + "password").then((data) => {
          this.password = data;
        });

        // if username and password exists show faio
        if (this.username != null && this.password != null) {
          this.faio.show({
            clientId: LoginPage.FAIO_SECRET,
            clientSecret: LoginPage.FAIO_SECRET,
            disableBackup: true
          }).then(() => {
            this.login();
          })
        }
      });
    }

  }

  /**
   * prevent login in screen to show up in browser if user is authenticated
   */
  ionViewWillEnter() {
    this.redirectUser();
  }

  /**
   * save username and password login
   */
  private saveToSecureStorage() {
    if (this.platform.is("ios") && this.saveLogin) {
      this.secureStorage.create(LoginPage.STORAGE_NAME).then((storage: SecureStorageObject) => {

        storage.set(LoginPage.STORAGE_PREFIX + "username", this.username);
        storage.set(LoginPage.STORAGE_PREFIX + "password", this.password);

      })
    }
  }

  /**
   * login user
   * redirect to DeskOverviewPage if successful
   */
  public login() {
    this.userService.login(this.username, this.password).then(() => {
      // save user to
      this.saveToSecureStorage();
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
