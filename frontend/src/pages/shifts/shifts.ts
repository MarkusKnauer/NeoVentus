import {Component} from "@angular/core";
import {LoadingController, NavController} from "ionic-angular";
import {WorkingShiftService} from "../../service/workingShifts.service";
import {AuthGuardService} from "../../service/auth-guard.service";
/**
 * @author Markus Knauer
 * @version 0.0.1
 */

@Component({
  selector: 'shifts-message',
  templateUrl: 'shifts.html'
})
export class ShiftsPage {
  private loading;

 public event =  {
   startDate: new Date().toISOString(),
   endDate: new Date().toISOString(),

 };

  private newDay = [];
  private personalView = true;

  constructor(private authGuard: AuthGuardService, private workingPlanService: WorkingShiftService,  public loadingCtrl: LoadingController, public navCtrl: NavController) {
    if(this.authGuard.hasRole("ROLE_SERVICE")){
      this.getPersonalShifts();
    } else {
      this.getAllShifts();
    }


  }

  /**
   * Select the choosen View
   * @returns {undefined}
   */
  getRightShift(){
    console.debug("right shift");

    if(this.personalView){
      return this.getPersonalShifts();
    } else {
      return this.getAllShifts();
    }

}


  /**
   * Get all Shifts for the Date-space and grouped by Day
   */
  getAllShifts(){
    let shifts = [];
    this.newDay = [];
    let tmpDate: Date;
    this.presentLoadingDefault('Gesamtansicht wird geladen.');
    Promise.all([
      this.workingPlanService.periodShifts(this.event.startDate, this.event.endDate).then(
        workplan => {
          for(let plan of workplan){
            shifts = [];
            for(let shift of plan.workingshift){
              shifts.push({startShift: new Date(shift.startShift), endShift: new Date(shift.endShift), userName: shift.user.username});
              console.debug("WIe oft?");
            }
            this.sortShiftsByDate(shifts);

            console.debug("Wie oft hier?");
            tmpDate = new Date(plan.workingDay);
            this.newDay.push({shifts: shifts, date: tmpDate});
          }


        }
      )
    ]).then(() => {
      this.loading.dismissAll();
    });




  }

  /**
   * Personal view for Logined User
   */
  getPersonalShifts(){
    let shifts = [];
    let tmpDate: Date;
    this.newDay = [];
    this.presentLoadingDefault('Deine persönliche Ansicht wird geladen.');
    Promise.all([
    this.workingPlanService.userPeriodShifts(this.authGuard.userDetails.name,this.event.startDate,this.event.endDate).then(
      workshift => {
          for(let shift of workshift){
            shifts = [];
            tmpDate = new Date(shift.startShift);
            shifts.push({startShift: new Date(shift.startShift), endShift: new Date(shift.endShift), userName: shift.user.username});
            this.newDay.push({shifts: shifts, date: tmpDate});
          }

      }
    )
    ]).then(() => {
      this.loading.dismissAll();
    });
  }

  /**
   * Sorts the Shifts in one Day by Date
   * @param shifts
   */
  sortShiftsByDate(shifts: any){
    shifts.sort((shift1,shift2) => shift1.startShift - shift2.startShift);
  }


  /**
   * Writes die Headline of Shifts
   * @returns {string}
   */
  headlineString(){
    let out: string;
    let dat1 = Date.parse(this.event.startDate);
    let dat2 = Date.parse(this.event.endDate);
    if(this.personalView){
      out = "Mein Schichtplan";
    } else{
      out = "Gesamtschichtplan";
    }
    return out;
  }

  /**
   * Parsing Date in German Date Notion DD.MM.YYYY
   * @param date
   * @returns {string}
   */
  getDate(date: Date){
    let day:string = "";
    let month:string = "";
    if(date.getMonth()+1 < 10){
      month = "0";
    }
    if(date.getDate()+1 < 10){
      day = "0";
    }

    return day+date.getDate()+"."+month+(date.getMonth()+1)+"."+date.getFullYear()
  }

  /**
   * Get 24 h Daytime in Format HH:MM (
   * @param date
   * @returns {string}
   */
  getDateTime(date: Date){
    let hours: string = "";
    let minutes: string= "";
    if(date.getHours()< 10)
      hours = "0";
    if(date.getMinutes()<10)
      minutes = "0";
    return hours+date.getHours()+":"+minutes+date.getMinutes();
  }

  toggleMyShiftsOnly = function () {
    this.personalView = !this.personalView;
    this.getRightShift();
  };

  compareDates(){
    let compare: any;
    let dat1 = Date.parse(this.event.startDate);
    let dat2 = Date.parse(this.event.endDate);
    compare = dat1- dat2;
    if(compare == 0 ){
     return 0;
    } else if(compare < 0 ){
      return -1;
    } else if(compare > 0 ){
      return 1;
    }
  }

  /**
   * show loading popup
   *
   * @param info info message to display
   */
  presentLoadingDefault(info) {
    this.loading = this.loadingCtrl.create({
      content: info
    });

    this.loading.present();
  }

}
