/*
 Generated class for the BeaconProvider provider.

 See https://angular.io/docs/ts/latest/guide/dependency-injection.html
 for more info on providers and Angular 2 DI.
 */
import {AuthGuardService} from "./auth-guard.service";
import {Http} from "@angular/http";
import {AlertController, Events, Platform} from "ionic-angular";
import {BeaconRegion, IBeacon} from "@ionic-native/ibeacon";
import {Injectable} from "@angular/core";
import {DevicePermissions} from "./device-permission.service";
import {CachingService} from "./caching.service";
import {Storage} from "@ionic/storage";
import {LocationAccuracy} from "ionic-native";
import {Diagnostic} from "@ionic-native/diagnostic";


@Injectable()
export class BeaconService extends CachingService{

  region: BeaconRegion;
  delegate: any;
  static isActivated:boolean = false;
  public static iBeaconIsEnabled = "beaconAllowed";


  constructor( public diagnostic: Diagnostic,private storage: Storage,public devicePermissions: DevicePermissions,private ibeacon: IBeacon,public platform: Platform, public events: Events, private http: Http, private authGuard: AuthGuardService , private alertCtrl: AlertController) {
    super();


  }

  startBeacon(deskRegion: string): any {
    let promise = new Promise((resolve, reject) => {
      // we need to be running on a device
      if (this.platform.is('cordova')) {
        console.error("STARTING BEACONS", this.platform);
        // Request permission to use location on iOS
        this.ibeacon.requestAlwaysAuthorization();

        console.info("DeskRegion: "+deskRegion);
        this.region = this.ibeacon.BeaconRegion('deskBeacon', deskRegion);
        // create a new delegate and register it with the native layer
        this.delegate = this.ibeacon.Delegate();
        // Subscribe to some of the delegate's event handlers
        console.info("Region: " + this.region);
        this.delegate.didRangeBeaconsInRegion()
          .subscribe(
            beaconData => {
              console.info("didRangeBeaconsINRegion: " + beaconData.beacons.length);
              this.events.publish('didRangeBeaconsInRegion', beaconData);
            },
            error => console.error()
          );

        // setup a beacon region

        this.ibeacon.startRangingBeaconsInRegion(this.region).then(
          () => {
            resolve(true);
          },
          error => {
            console.error('Failed to begin monitoring: ', error);
            resolve(false);
          }
        );
      } else {
        console.error("This application needs to be running on a device");
        resolve(false);
      }
    });

    return promise;
  }

  turnBLEAndGPSOn(){
    this.diagnostic.getBluetoothState()
      .then((state) => {
        if (state == this.diagnostic.bluetoothState.POWERED_OFF){
          this.ibeacon.enableBluetooth();
        }
      }).catch(e => console.error(e));


    LocationAccuracy.canRequest().then((canRequest: boolean) => {
      if(canRequest) {
        // the accuracy option will be ignored by iOS
        LocationAccuracy.request(LocationAccuracy.REQUEST_PRIORITY_HIGH_ACCURACY).then(
          () => console.log('Request successful'),
          error => console.log('Error requesting location permissions', error)
        );
      }
    });
  }

  turnBLEAndGPSOff(){
    this.diagnostic.getBluetoothState()
      .then((state) => {
        if (state != this.diagnostic.bluetoothState.POWERED_OFF){
          this.ibeacon.disableBluetooth();
        }
      }).catch(e => console.error(e));


    LocationAccuracy.canRequest().then((canRequest: boolean) => {
      if(canRequest) {
        // the accuracy option will be ignored by iOS
        LocationAccuracy.request(LocationAccuracy.REQUEST_PRIORITY_NO_POWER).then(
          () => console.log('Request successful'),
          error => console.log('Error requesting location permissions', error)
        );
      }
    });
  }





  stopRangingRegion(): any {
  let promise = new Promise((resolve, reject) => {
  // we need to be running on a device
  if (this.platform.is('cordova')) {
    // setup a beacon region
    this.ibeacon.stopRangingBeaconsInRegion(this.region).then(
      () => {
        resolve(true);
      },
      error => {
        console.error('Failed to begin monitoring: ', error);
        resolve(false);
      }
    );


      } else {
        console.error("This application needs to be running on a device");
        resolve(false);
      }
    });

    return promise;
  }


  startRangingRegion(): any {
    let promise = new Promise((resolve, reject) => {
      if (this.platform.is('cordova')) {
        this.ibeacon.startRangingBeaconsInRegion(this.region).then(
          () => {
            resolve(true);
          },
          error => {
            console.error('Failed to begin monitoring: ', error);
            resolve(false);
          }
        );;

      } else{
        console.error("This application needs to be running on a device");
      }
    });
    return promise;
  }

  /**
   * Cache things
   */
  loadBeaconBoolean(){
    return this.storage.ready().then(() => {

      return this.storage.get(BeaconService.iBeaconIsEnabled).then((val) => {
        this.saveToCache(BeaconService.iBeaconIsEnabled, JSON.parse(val));
      })
    })


  }
  /**
   * save cached data
   * @param key
   */
  saveData(key: string, data: string) {
    this.storage.ready().then(() => {
      this.storage.set(key, data);
    });
  }

  /**
   * private save cached json data
   * @param key
   */
  saveCachedJsonData(key: string) {
    this.storage.ready().then(() => {
      this.storage.set(key, JSON.stringify(this.cache[key]));
    });

  }
}
