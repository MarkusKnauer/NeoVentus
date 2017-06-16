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
        if (!this.isBluetoothON()) {
          this.diagnostic.switchToBluetoothSettings();
        }
  }


  //Pop-up mit nachfrage <-- TODO
  checkIfGPSIsOnAndSetItOn(): void{
        if (!this.isLocationON()){
          this.diagnostic.switchToLocationSettings();
        }

  }
  checkIfBluetoothIsOFFAndSetItOn(): void{
    if (this.isBluetoothON()) {
      this.diagnostic.switchToBluetoothSettings();
    }
  }


  //Pop-up mit nachfrage <-- TODO
  checkIfGPSIsOFFAndSetItOn(): void{
    if (this.isLocationON()){
      this.diagnostic.switchToLocationSettings();
    }

  }
  isBluetoothON():boolean{
    this.diagnostic.getBluetoothState()
      .then((state) => {
        return (state == this.diagnostic.bluetoothState.POWERED_OFF);
      }).catch(e => console.error(e));
    return true;
  }

  isLocationON():boolean{
  this.diagnostic.getLocationMode()
      .then((state) => {
         return (state == this.diagnostic.locationMode.LOCATION_OFF);
      }).catch(e => console.error(e));
    return true;
  }


}
