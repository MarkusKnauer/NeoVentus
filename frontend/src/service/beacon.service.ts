
/*
 Generated class for the BeaconProvider provider.

 See https://angular.io/docs/ts/latest/guide/dependency-injection.html
 for more info on providers and Angular 2 DI.
 */
import {AuthGuardService} from "./auth-guard.service";
import {Http} from "@angular/http";
import {Events, Platform} from "ionic-angular";
import {IBeacon} from "@ionic-native/ibeacon";
import {Injectable} from "@angular/core";
@Injectable()
export class BeaconService {

  delegate: any;
  region: any;

  constructor(public platform: Platform, public events: Events,private http: Http, private authGuard: AuthGuardService, private ibeacon: IBeacon) {

  }

  initialise(): any {
    let promise = new Promise((resolve, reject) => {
// we need to be running on a device
      if (this.platform.is("cordova")) {

// Request permission to use location on iOS
        this.ibeacon.requestAlwaysAuthorization();

// create a new delegate and register it with the native layer
        this.delegate =  this.ibeacon.Delegate();

// Subscribe to some of the delegate"s event handlers
        this.delegate.didRangeBeaconsInRegion()
          .subscribe(
            data => {
              this.events.publish("didRangeBeaconsInRegion", data);
            },
            error => console.error()
          );

// setup a beacon region – CHANGE THIS TO YOUR OWN UUID
        this.region =  this.ibeacon.BeaconRegion("deskBeacon", "D2C56DB5-DFFB-48D2-B060-D0F5A71096E0");

// start ranging
        this.ibeacon.startRangingBeaconsInRegion(this.region)
          .then(
            () => {
              resolve(true);
            },
            error => {
              console.error("Failed to begin monitoring: ", error);
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
}
