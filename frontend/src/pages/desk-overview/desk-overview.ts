import {Component, NgZone} from "@angular/core";
import {AlertController, Events, NavController, Platform} from "ionic-angular";
import {DeskService} from "../../service/desk.service";
import {AuthGuardService} from "../../service/auth-guard.service";
import {LoginPage} from "../login/login";
import {DeskPage} from "../desk/desk";
import {BeaconService} from "../../service/beacon.service";
import {BeaconModel} from "../../model/beacon-module";

/**
 * @author Tim Heidelbach, Dennis Thanner
 */
@Component({
  templateUrl: "desk-overview.html",
})
export class DeskOverviewPage {

  private tileView = true;
  private myDesksOnly = false;
  private user = null;
  private desks: any;

  beacons: BeaconModel[] = [];
  zone: any;
  static actualBeaconUUID: string = "";
  isInitialiseBeacon: boolean = false;



  constructor(private navCtrl: NavController,
              private deskService: DeskService,
              private authGuard: AuthGuardService,
              private events: Events,
              public platform: Platform,
              public beaconService: BeaconService,
              private alertCtrl: AlertController) {

    // required for UI update
    this.zone = new NgZone({enableLongStackTrace: false});

    this.loadDeskDetails();

    // listen to billing changes and reload desk data
    this.events.subscribe("order-change", () => {
      console.debug("reload desk overview data for desk after billing");
      this.loadDeskDetails();
    });

  }

  private loadDeskDetails() {
    this.deskService.getAllDesksWithDetails().then(
      desks => {
        this.desks = desks;

        this.getUserName();
        for (let desk of desks) {

          DeskOverviewPage.actualBeaconUUID = desk.beaconUUID;
          for (let waiter of desk.waiters) {
            if (waiter == this.user) {
              desk.mine = true;
              break;
            }
          }
        }


          if(!this.isInitialiseBeacon){
          this.beaconService.startBeacon(DeskOverviewPage.actualBeaconUUID).then((isInitialised) => {
            if (isInitialised) {

                this.listenToBeaconEvents();
                this.isInitialiseBeacon = true;

            }});
          } else{
            this.beaconService.startRangingRegion();
          }

      }
    );

  }

  /**
   * view life cycle method
   *
   * RBMA
   */
  ionViewWillEnter() {
    this.authGuard.hasAnyRolePromise(["ROLE_CEO", "ROLE_SERVICE"]).then(() => {
        if(DeskOverviewPage.actualBeaconUUID !== ""){
          this.beaconService.startRangingRegion();
        }
    }, () => {
      console.debug("RBMA - Access denied!");
      this.navCtrl.setRoot(LoginPage);
    });
  }


  ionViewWillLeave(){
    this.beaconService.stopRangingRegion();
  }




  private deskSelected(desk) {
    this.beaconService.stopRangingRegion();
    this.navCtrl.push(DeskPage, {deskNumber: desk.deskNumber.toString()});

  }

  private toggleView() {
    this.tileView ? this.tileView = false : this.tileView = true;
  }

  private toggleMyDesksOnly() {

    if (this.myDesksOnly) {
      this.myDesksOnly = false;
      console.log("all desks");

    } else {
      console.log("my desks only");
      this.myDesksOnly = true;
    }
  };

  private getUserName() {
    if (this.user == null) {
      try {
        this.user = this.authGuard.userDetails.name;
      } catch (exception) {
      }
    }
    return this.user;
  }


  listenToBeaconEvents() {
    this.events.subscribe('didRangeBeaconsInRegion', (beaconData) => {

// update the UI with the beacon list
      this.zone.run(() => {

        this.beacons = [];

        let beaconList = beaconData.beacons;

        beaconList.forEach((beacon) => {

          let beaconObject = new BeaconModel(beacon);

          this.beacons.push(beaconObject);
        });
        this.checkNearestBeacon(this.beacons);
      });

    });
  }
  checkNearestBeacon(beacons: BeaconModel[]){
    let beaconTMP: BeaconModel;
    for(let beac of beacons){
      if(beaconTMP == null){beaconTMP = beac;}
      if(beac.rssi > beaconTMP.rssi) beaconTMP = beac;
    }

    if(beaconTMP != null){
      if(beaconTMP.rssi >  -50) {
        this.findBeaconDesk(beaconTMP);
      }
    } else{
      console.info("DANGER!! No Beacons found!!");
    }
  }
  findBeaconDesk(beacon: BeaconModel){
    // DB- search for UUID+Major+Minor
    let fullID : string;
    fullID = beacon.uuid+beacon.major+beacon.minor;

    // Service search
      let beaconDesk : any;
      for(let desk of this.desks){
        if(desk.beaconUUID.toUpperCase() === beacon.uuid.toUpperCase() &&
        desk.beaconMajor.toUpperCase() === beacon.minor.toUpperCase() &&
        desk.beaconMinor.toUpperCase() === beacon.minor.toUpperCase()){
          beaconDesk = desk;
        }
    }

    // Push to desk
    if(beaconDesk != null){
      this.deskSelected(beaconDesk);
    }

  }

}
