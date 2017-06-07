
/*
 Generated class for the BeaconProvider provider.

 See https://angular.io/docs/ts/latest/guide/dependency-injection.html
 for more info on providers and Angular 2 DI.
 */
import {AuthGuardService} from "./auth-guard.service";
import {Http} from "@angular/http";
import {AlertController, Events, Platform} from "ionic-angular";
import {IBeacon} from '@ionic-native/ibeacon';
import {Injectable} from "@angular/core";
@Injectable()
export class BeaconService {

  constructor(private ibeacon: IBeacon,public platform: Platform, public events: Events, private http: Http, private authGuard: AuthGuardService , private alertCtrl: AlertController) {

  }

  initialise(): any {
    let promise = new Promise((resolve, reject) => {
      // we need to be running on a device
      if (this.platform.is('cordova')) {

        // Request permission to use location on iOS
        this.ibeacon.requestAlwaysAuthorization();

        // create a new delegate and register it with the native layer
        let delegate = this.ibeacon.Delegate();
        // Subscribe to some of the delegate's event handlers
        delegate.didRangeBeaconsInRegion()
          .subscribe(
            beaconData => {
              let alert = this.alertCtrl.create({
                title: "BeaconResult: "+ beaconData.beacons[0].uuid,
                buttons: [
                  {
                    text: "Nein",
                    handler: () => {
                      alert.dismiss();
                    }
                  }]});
              alert.present();

              this.events.publish('didRangeBeaconsInRegion', beaconData);

            },
            error => console.error()
          );

        delegate.didStartMonitoringForRegion()
          .subscribe( test =>{
           let alert = this.alertCtrl.create({
          title: "BeaconMonitor: ",
          buttons: [
            {
              text: "Nein",
              handler: () => {
                alert.dismiss();
              }
            }]});
        alert.present(),

        data => console.log('didStartMonitoringForRegion: ', data),
            error => console.error()
          });
        // setup a beacon region
         let region = this.ibeacon.BeaconRegion('deskBeacon', '987B5028-30E2-4C08-B0B9-5AB16A57BE6B');

        // start ranging
        this.ibeacon.startRangingBeaconsInRegion(region)
          .then(
            () => {
              let alert = this.alertCtrl.create({
                title: "Ranging: "+  this.ibeacon.getRangedRegions()[0].identifier,
                buttons: [
                  {
                    text: "Nein",
                    handler: () => {
                      alert.dismiss();
                    }
                  }]});
              alert.present();

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
}
