import {Component, ViewChild} from "@angular/core";
import {LoadingController, NavController} from "ionic-angular";
import {WorkingShiftService} from "../../service/workingShifts.service";
import {AuthGuardService} from "../../service/auth-guard.service";
import {CalendarComponent, IEvent} from "ionic2-calendar/calendar";

/**
 * @author Markus Knauer
 * @author Dennis Thanner
 */
@Component({
  selector: 'shifts-page',
  templateUrl: 'shifts.html'
})
export class ShiftsPage {

  public calendar = {
    mode: "month",
    currentDate: new Date()
  };

  @ViewChild(CalendarComponent)
  private calendarComp: CalendarComponent;

  public data: IEvent[] = [];

  private personalView = true;

  private lastLoadedRange = {
    start: new Date(),
    end: new Date()
  };

  private title = "";

  constructor(private authGuard: AuthGuardService, private workingPlanService: WorkingShiftService,
              public loadingCtrl: LoadingController, public navCtrl: NavController) {
    if (this.authGuard.hasRole("ROLE_SERVICE")) {
      this.personalView = true;
    } else {
      this.personalView = false;
    }
  }

  /**
   * load shift data in given interval
   * @param start
   * @param end
   */
  loadData(start: Date, end: Date) {
    if (start != end) {
      this.lastLoadedRange.start = start;
      this.lastLoadedRange.end = end;
    }
    if (this.personalView) {
      this.workingPlanService.userPeriodShifts(this.authGuard.userDetails.name, start.toISOString(), end.toISOString()).then(data => {
        for (let shift of data) {
          this.addShiftToData(shift);
        }
        this.calendarComp.loadEvents();
      });
    } else {
      this.workingPlanService.periodShifts(start.toISOString(), end.toISOString()).then(data => {
        for (let detail of data) {
          for (let shift of detail.workingshift) {
            this.addShiftToData(shift)
          }
        }
        this.calendarComp.loadEvents();
      });
    }
  }

  /**
   * shift data to calendar and check for duplicate entries
   * @param shift
   */
  addShiftToData(shift) {
    let start = new Date(shift.startShift);
    let end = new Date(shift.endShift);
    let event: IEvent = {
      title: shift.user.username,
      startTime: start,
      endTime: end,
      allDay: false
    };
    //prevent multiple adding
    let duplicate = this.data.find(el => {
      return el.title == event.title && el.startTime.getTime() == event.startTime.getTime()
        && el.endTime.getTime() == event.endTime.getTime();
    });
    if (duplicate == null) {
      this.data.push(event)
    }
  }

  /**
   * change between personal and overall view
   */
  personalViewChange() {
    this.personalView = !this.personalView;
    // reset data
    this.data = [];
    this.loadData(this.lastLoadedRange.start, this.lastLoadedRange.end);
  }

}
