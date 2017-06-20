import {Component, ViewChild} from "@angular/core";
import {UserService} from "../../service/user.service";
import {BaseChartDirective} from "ng2-charts";
import {LocalStorageService} from "../../service/local-storage.service";

@Component({
  selector: "user-statistics",
  templateUrl: "tip-monitor.html"
})
export class TipMonitorPage {

  private tips: Array<any> = [
    {data: [], label: "Trinkgeld"},
    {data: [0, 0, 0, 0, 0, 0, 0], label: "tägliches Trinkgeld Ziel"},
  ];
  private tipsLabel = [];

  @ViewChild(BaseChartDirective)
  public chart: BaseChartDirective;

  public monthlyTipGoal: number = 0;

  constructor(private userService: UserService, private localStorageService: LocalStorageService) {
    this.userService.getLastWeekTips().then((resp) => {
      let data = resp.json();

      for (let key in data) {

        if (data.hasOwnProperty(key)) {
          this.tips[0].data.push(Number.parseFloat(data[key].toFixed(2)));
          this.tipsLabel.push(new Date(Number.parseFloat(key)).toLocaleDateString());
        }
      }
      // redraw
      this.chart.ngOnInit();

    })
  }

  public sumTips() {
    let sum = 0;
    for (let tip of this.tips[0].data) {
      sum += tip;
    }
    return sum;
  }

  private getDaysInCurrentMonth() {
    let d = new Date();
    let month = new Date(d.getFullYear(), d.getMonth() - 1, 0);
    return month.getDate();
  }

  updateDailyGoal() {
    console.info("Goal change");
    let newGoalData = [];
    for (let i of this.tips[0].data) {
      newGoalData.push(Number.parseFloat((this.monthlyTipGoal / this.getDaysInCurrentMonth()).toFixed(2)));
    }
    this.tips[1].data = newGoalData;
    this.chart.ngOnInit();
  }

  saveDailyGoal() {
    this.localStorageService.saveData(LocalStorageService.MONTHLY_TIP_GOAL, this.monthlyTipGoal.toFixed(2));
  }

}
