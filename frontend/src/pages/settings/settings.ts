import {Component} from "@angular/core";
import {
  AlertController, Events, LoadingController, ModalController, NavController, Platform,
  ToastController
} from "ionic-angular";
import {ManageStornoReasonsModalComponent} from "../../component/manage-storno-reasons/manage-storno-reasons";
import {LocalStorageService} from "../../service/local-storage.service";
import {Http} from "@angular/http";
import {AuthGuardService} from "../../service/auth-guard.service";
import {ApplicationEvents} from "../../app/events";
import {DevicePermissions} from "../../service/device-permission.service";
import {Diagnostic} from "@ionic-native/diagnostic";
import {BeaconService} from "../../service/beacon.service";

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

  public ibeaconIsON: boolean =false;


  constructor(public navCtrl: NavController, private modalCtrl: ModalController, private loadingCtrl: LoadingController,
              private localSorageService: LocalStorageService, private http: Http, private events: Events,
              private toastCtrl: ToastController, public authGuard: AuthGuardService, public platform: Platform,
              private alertCtrl: AlertController,
              public devicePermissions: DevicePermissions,
              public diagnostic: Diagnostic,
              public beaconService: BeaconService,
  ) {

    //Get Beacons from Cache
    this.beaconService.loadBeaconBoolean().then(()=>{
      BeaconService.isActivated =  this.ibeaconIsON = this.beaconService.cache[BeaconService.iBeaconIsEnabled];
      if(this.ibeaconIsON == null){
        this.ibeaconIsON = false;
      }
    });

    this.localSorageService.loadConnectionUrl().then(() => {
      this.connectionUrl = this.localSorageService.cache[LocalStorageService.CONNECTION_URL];
      if (this.connectionUrl == null) {
        this.connectionUrl = "http://";
      }
    })
  }

  //Toggle Function for change things
  toggleBeaconSetting = function () {
     this.ibeaconIsON = !this.ibeaconIsON;
    if (this.platform.is('cordova')){
      this.changeIBeaconMode();
    }
    this.beaconService.saveData(BeaconService.iBeaconIsEnabled,this.ibeaconIsON);
    BeaconService.isActivated = this.ibeaconIsON;
  };

  /**
   * 1. if: Beacon Toggler is 'true'
   * => Yes, 1.1 if Autohorisation not allowed
   *  =>  Yes, 1.1.1 Alert-if  wanna beacons in da phone
   *    => Yes, Allow it!
   *    => No, go back.
   *  => No, set GPS and Bluetooth on
   * => No, 1.2 Alert-if wanna turn off BLE and GPS
   *
   */
  changeIBeaconMode(){

    if(this.ibeaconIsON){

      //Check if Authorisation is Valid
      if(!(this.diagnostic.isLocationAuthorized())){
          let alert = this.alertCtrl.create({
            title: "Möchten Sie IBeacons erlauben (Bluetooth und Standort)?",
            buttons: [
              {
                text: "Nein",
                handler: () => {
                  this.ibeaconIsON = !this.ibeaconIsON;
                  alert.dismiss();
                }
              },
              {
                text: "Ja",
                handler: () => {

                  // Set Authorisation and GPS and Bluettoth true

                  this.diagnostic.getBluetoothState()
                    .then((state) => {
                      if (state == this.diagnostic.bluetoothState.POWERED_OFF){
                        this.diagnostic.switchToBluetoothSettings();
                      }
                    }).catch(e => console.error(e));

                  this.diagnostic.getLocationMode()
                    .then((state) => {
                      if (state == this.diagnostic.locationMode.LOCATION_OFF){
                        this.diagnostic.switchToLocationSettings();
                      }
                    }).catch(e => console.error(e));

                  alert.dismiss();
                }
              }
            ],
          });
          alert.present();
        } else{
     // Is Authorisation valid and set GPS and Bluettoth true.
        this.diagnostic.getBluetoothState()
          .then((state) => {
            if (state == this.diagnostic.bluetoothState.POWERED_OFF){
              this.diagnostic.switchToBluetoothSettings();
            }
          }).catch(e => console.error(e));

        this.diagnostic.getLocationMode()
          .then((state) => {
            if (state == this.diagnostic.locationMode.LOCATION_OFF){
              this.diagnostic.switchToLocationSettings();
            }
          }).catch(e => console.error(e));
       }
      } else {
      // turn Ibeacons off

      let alert = this.alertCtrl.create({
        title: "Möchten Sie Bluetooth und Standort auch ausschalten?",
        buttons: [
          {
            text: "Nein",
            handler: () => {
              alert.dismiss();
            }
          },
          {
            text: "Ja",
            handler: () => {
              this.diagnostic.getBluetoothState()
                .then((state) => {
                  if (state !== this.diagnostic.bluetoothState.POWERED_OFF){
                    this.diagnostic.switchToBluetoothSettings();
                  }
                }).catch(e => console.error(e));

              this.diagnostic.getLocationMode()
                .then((state) => {
                  if (state !== this.diagnostic.locationMode.LOCATION_OFF){
                    this.diagnostic.switchToLocationSettings();
                  }
                }).catch(e => console.error(e));
              alert.dismiss();
            }
          }
        ],
      });
      alert.present();
    }
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
      loading.dismiss();
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
