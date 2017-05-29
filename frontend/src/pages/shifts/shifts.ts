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

  private tileView = true;
  private myDesksOnly = false;

  constructor(private authGuard: AuthGuardService, private workingPlanService: WorkingShiftService,  public loadingCtrl: LoadingController, public navCtrl: NavController) {
    this.getPersonalShifts();

  }


getRightShift(){
    console.debug("right shift");
    if(this.personalView){
      return this.getPersonalShifts();
    } else {
      return this.getShifts();
    }

}

  getShifts(){
    let shifts = [];
    this.newDay = [];
    let tmpDate: String;
    this.workingPlanService.periodShifts(this.event.startDate, this.event.endDate).then(
      workplan => {
        for(let plan of workplan){
          shifts = [];
          for(let shift of plan.workingshift){
            shifts.push({startShift: new Date(shift.startShift).toLocaleString(), endShift: new Date(shift.endShift).toLocaleString(), userName: shift.user.username});
            console.debug("WIe oft?");
          }
          this.sortShiftsByDate(shifts);

          console.debug("Wie oft hier?");
          tmpDate = new Date(plan.workingDay).toLocaleString();
          this.newDay.push({shifts: shifts, date: tmpDate});
        }


      }
    );

  }

  getPersonalShifts(){
    let shifts = [];
    let tmpDate: String;
    this.newDay = [];
    this.workingPlanService.userPeriodShifts(this.authGuard.userDetails.name,this.event.startDate,this.event.endDate).then(
      workshift => {
          for(let shift of workshift){
            shifts = [];
            tmpDate = new Date(shift.startShift).toLocaleString();
            shifts.push({startShift: new Date(shift.startShift).toLocaleString(), endShift: new Date(shift.endShift).toLocaleString(), userName: shift.user.username});
            this.newDay.push({shifts: shifts, date: tmpDate});
          }

      }
    );
    console.debug("NewDay: "+ this.newDay.length);
    return "ok";
  }

  sortShiftsByDate(shifts: any){
    shifts.sort((shift1,shift2) => shift1.startShift - shift2.startShift);
  }


  headlineString(){
    let out: string;
    let dat1 = Date.parse(this.event.startDate);
    let dat2 = Date.parse(this.event.endDate);
    if(this.personalView){
      out = "Schichtplan von "+this.authGuard.userDetails.name + "\n";
    } else{
      out = "Gesamtschichtplan ";
    }
    switch(this.compareDates()){
      case 0:
        out += "für den Tag: "+ new Date(dat1).toLocaleString().substring(0,9);
        break;
      case 1:
        out = "Der Zeitraum ist ungültig, bitte überprüfen Sie Ihre Eingabe!!";
        break;
      case -1:
        out += "für den Zeitraum von "+new Date(dat1).toLocaleString().substring(0,9)+ " bis "+ new Date(dat2).toLocaleString().substring(0,9);
        break;
    }
    return out;
  }


  midLineString(){
   this.getPersonalShifts();
   return "Test";
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
