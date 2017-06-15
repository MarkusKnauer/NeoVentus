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


@Injectable()
export class BeaconService {

  region: BeaconRegion;
  delegate: any;


  constructor(public devicePermissions: DevicePermissions, private ibeacon: IBeacon, public platform: Platform, public events: Events, private http: Http, private authGuard: AuthGuardService, private alertCtrl: AlertController) {
    this.devicePermissions.checkLocationPermissions();
  }

  startBeacon(deskRegion: string): any {
    let promise = new Promise((resolve, reject) => {
      // we need to be running on a device
      if (this.platform.is('cordova')) {
        console.error("STARTING BEACONS", this.platform);
        // Request permission to use location on iOS
        this.ibeacon.requestAlwaysAuthorization();

        //activate Bluetooth
        this.enableBeaconPermission();

        console.info("DeskRegion: " + deskRegion);
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


  enableBeaconPermission() {
    this.devicePermissions.checkIfBluetoothIsOnAndSetItOn();
    this.devicePermissions.checkIfGPSIsOnAndSetItOn();
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
    if (this.platform.is("cordova"))
      this.ibeacon.startRangingBeaconsInRegion(this.region);
  }
}
