import {Component} from "@angular/core";
import {Events, LoadingController, ModalController, NavController, Platform, ToastController} from "ionic-angular";
import {ManageStornoReasonsModalComponent} from "../../component/manage-storno-reasons/manage-storno-reasons";
import {LocalStorageService} from "../../service/local-storage.service";
import {Http} from "@angular/http";
import {AuthGuardService} from "../../service/auth-guard.service";
import {ApplicationEvents} from "../../app/events";

/**
 * @author Markus Knauer, Dennis Thanner
 */

@Component({
  selector: 'settings-page',
  templateUrl: 'settings.html'
})
export class SettingsPage {

  private editConnection: false;

  private connectionUrl: string = "http://";

  constructor(public navCtrl: NavController, private modalCtrl: ModalController, private loadingCtrl: LoadingController,
              private localSorageService: LocalStorageService, private http: Http, private events: Events,
              private toastCtrl: ToastController, public authGuard: AuthGuardService, public platform: Platform) {
    this.localSorageService.loadConnectionUrl().then(() => {
      this.connectionUrl = this.localSorageService.cache[LocalStorageService.CONNECTION_URL];
      if (this.connectionUrl == null) {
        this.connectionUrl = "http://";
      }
    })
  }

  /**
   * open modal to edit storno reasons
   */
  openStornoReasonsModal() {
    this.modalCtrl.create(ManageStornoReasonsModalComponent).present();
  }

  /**
   * check connection and save it
   */
  saveConnectionUrl() {
    if (!this.connectionUrl.startsWith("http://")) {
      this.connectionUrl = "http://" + this.connectionUrl;
    }
    let loading = this.loadingCtrl.create({
      content: "Verbindung wird überprüft"
    });
    loading.present();
    this.checkIfConnectionIsOk().then((valid) => {
      let toast;
      if (valid) {
        this.localSorageService.saveData(LocalStorageService.CONNECTION_URL, this.connectionUrl);
        this.editConnection = false;
        // emit event to update services
        this.events.publish(ApplicationEvents.CONNECTION_CHANGE_EVENT, this.connectionUrl);
        // give user feedback
        toast = this.toastCtrl.create({
          message: "Verbindung erfolgreich!",
          duration: 3000
        })
      } else {
        toast = this.toastCtrl.create({
          message: "Verbindung fehlgeschlagen! Wenden Sie sich an Ihren Arbeitgeber.",
          duration: 3000
        })
      }
      toast.present();
      loading.dismissAll();
    })
  }

  /**
   * short check if login resource exists
   *
   * @returns {Promise<boolean|boolean>}
   */
  private checkIfConnectionIsOk() {
    return this.http.post(this.connectionUrl + "/auth/login", {},).timeout(3000).toPromise().then(() => {
      return true;
    }, (resp) => {
      if (resp.name == "TimeoutError") {
        return false;
      } else {
        console.log(resp.json());
        // login page without data should return not authorized status 401
        return resp.status == 401;
      }
    })
  }

}
