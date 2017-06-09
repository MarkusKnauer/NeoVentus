import {Component} from "@angular/core";
import {NavController, Platform, ToastController} from "ionic-angular";
import {FingerprintAIO} from "@ionic-native/fingerprint-aio";
import {SecureStorage, SecureStorageObject} from "@ionic-native/secure-storage";
import {UserService} from "../../service/user.service";
import {DeskOverviewPage} from "../desk-overview/desk-overview";
import {AuthGuardService} from "../../service/auth-guard.service";
import {KitchenOverviewPage} from "../kitchen-overview/kitchen-overview";
import {LocalStorageService} from "../../service/local-storage.service";
import {SettingsPage} from "../settings/settings";

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

  private saveLoginAvailable = false;

  private connectionUrl: string = null;

  constructor(public navCtrl: NavController, private userService: UserService, private authGuard: AuthGuardService,
              private toastCtrl: ToastController, private faio: FingerprintAIO, private secureStorage: SecureStorage,
              private platform: Platform, private localStorageService: LocalStorageService) {
    // on cordova look if username and password is saved in secure storage
    if (platform.is("cordova")) {
      // check connection url
      this.localStorageService.loadConnectionUrl().then(() => {
        this.connectionUrl = this.localStorageService.cache[LocalStorageService.CONNECTION_URL];
      });

      // fingerprint auth
      this.faio.isAvailable().then(() => {
        this.saveLoginAvailable = true;
        this.secureStorage.create(LoginPage.STORAGE_NAME).then((storage: SecureStorageObject) => {

          Promise.all([
            storage.get(LoginPage.STORAGE_PREFIX + "username").then((data) => {
              this.username = data;
            }, (err) => {
            }),
            storage.get(LoginPage.STORAGE_PREFIX + "password").then((data) => {
              this.password = data;
            }, (err) => {
            })
          ]).then(() => {
            // if username and password exists show faio
            if (this.username != null && this.password != null && this.username.length && this.password.length) {

              this.faio.show({
                clientId: LoginPage.FAIO_SECRET,
                clientSecret: LoginPage.FAIO_SECRET,
                disableBackup: true
              }).then(() => {
                this.login();
              }, () => {
                console.debug("fingerprint canceled")
              });

            }
          });
        });
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
    if (this.platform.is("cordova") && this.saveLogin) {
      this.secureStorage.create(LoginPage.STORAGE_NAME).then((storage: SecureStorageObject) => {
        console.debug("Saving user credentials to secure storage");
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
   * open settings page
   */
  private openSettingsPage() {
    this.navCtrl.push(SettingsPage);
  }

  /**
   * redirect user to specific pages after login
   */
  private redirectUser() {
    this.authGuard.isAuthenticatedPromise().then(() => {
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
