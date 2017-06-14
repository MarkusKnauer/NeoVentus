import { Platform} from "ionic-angular";
import {Injectable} from "@angular/core";
import { Diagnostic } from '@ionic-native/diagnostic';

/**
 * Permissions for Devices IOS and Android - JB
 */

@Injectable()
export class DevicePermissions {

  constructor(public diagnostic: Diagnostic,public platform: Platform) {

  }


  isAndroid() {
    return this.platform.is('android') && this.platform.is("cordova");
  }

  isiOS() {
    return this.platform.is('ios') && this.platform.is("cordova");
    ;
  }

  isUndefined(type) {
    return typeof type === "undefined";
  }


// Locationpermission
  checkLocationPermissions(): Promise<boolean> {
    return new Promise(resolve => {
     if (this.isiOS()) {
        this.diagnostic.getLocationAuthorizationStatus().then(status => {
          if (status == this.diagnostic.permissionStatus.GRANTED) {
            resolve(true);
          }
          else if (status == this.diagnostic.permissionStatus.DENIED) {
            resolve(false);
          }
          else if (status == this.diagnostic.permissionStatus.NOT_REQUESTED || status.toLowerCase() == 'notdetermined') {
            this.diagnostic.requestLocationAuthorization().then(authorisation => {
              resolve(authorisation == this.diagnostic.permissionStatus.GRANTED);
            });
          }
        });
      }
      else if (this.isAndroid()) {
        this.diagnostic.isLocationAuthorized().then(authorised => {
          if (authorised) {
            resolve(true);
          }
          else {
            this.diagnostic.requestLocationAuthorization().then(authorisation => {
              resolve(authorisation == this.diagnostic.permissionStatus.GRANTED);
            });
          }
        });
      }
    });
  }

  checkIfBluetoothIsOnAndSetItOn(): void{
    this.diagnostic.getBluetoothState()
      .then((state) => {
        if (state == this.diagnostic.bluetoothState.POWERED_OFF){
          this.diagnostic.switchToBluetoothSettings();
        }
      }).catch(e => console.error(e));

  }


  //Pop-up mit nachfrage <-- TODO
  checkIfGPSIsOnAndSetItOn(): void{
    this.diagnostic.getLocationMode()
      .then((state) => {
        if (state == this.diagnostic.locationMode.LOCATION_OFF){
          this.diagnostic.switchToLocationSettings();
        }
      }).catch(e => console.error(e));
  }

}
