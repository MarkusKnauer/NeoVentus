
/*
 Generated class for the BeaconProvider provider.

 See https://angular.io/docs/ts/latest/guide/dependency-injection.html
 for more info on providers and Angular 2 DI.
 */
import {AuthGuardService} from "./auth-guard.service";
import {Http} from "@angular/http";
import {AlertController, Events, Platform} from "ionic-angular";
import {IBeacon} from 'ionic-native';
import {Injectable} from "@angular/core";
@Injectable()
export class BeaconService {

  delegate: any;
  region: any;

  constructor(public platform: Platform, public events: Events, private http: Http, private authGuard: AuthGuardService , private alertCtrl: AlertController) {

  }

  initialise(): any {
    let promise = new Promise((resolve, reject) => {
      // we need to be running on a device
      if (this.platform.is('cordova')) {

        // Request permission to use location on iOS
        IBeacon.requestAlwaysAuthorization();

        // create a new delegate and register it with the native layer
        this.delegate = IBeacon.Delegate();
        // Subscribe to some of the delegate's event handlers
        this.delegate.didRangeBeaconsInRegion()
          .subscribe(
            beaconData => {
              this.events.publish('didRangeBeaconsInRegion', beaconData);
            },
            error => console.error()
          );

        // setup a beacon region
        this.region = IBeacon.BeaconRegion('deskBeacon', '987B5028-30E2-4C08-B0B9-5AB16A57BE6B');

        // start ranging
        IBeacon.startRangingBeaconsInRegion(this.region)
          .then(
            () => {
              let alert = this.alertCtrl.create({
                title: 'Beacon richtig erkannt!',

                buttons: [
                  {
                    text: 'Abbruch',
                    role: 'cancel',
                  }
                ]
              });
              alert.present();
              resolve(true);
            },
            error => {
              console.error('Failed to begin monitoring: ', error);
              resolve(false);
            }
          );


      } else {
        let alert = this.alertCtrl.create({
          title: 'Divice nicht erkannt!',

          buttons: [
            {
              text: 'Abbruch',
              role: 'cancel',
            }
          ]
        });
        alert.present();
        console.error("This application needs to be running on a device");
        resolve(false);
      }
    });

    return promise;
  }
}
