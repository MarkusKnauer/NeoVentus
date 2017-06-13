export class BeaconModel {

  uuid: string;
  major: string;
  minor: string;
  rssi: number;

  constructor(public beacon: any) {
    this.uuid = beacon.uuid;
    this.major = this.fillHex(beacon.major.toString());
    this.minor = this.fillHex(beacon.minor.toString());
    this.rssi = beacon.rssi;
  }

  private fillHex(value: string){
    for( let i = value.length; i < 4; i++){
      value = "0"+value;
    }
    return value;
  }

}
