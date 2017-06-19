import {Component, ViewChild} from "@angular/core";
import {UserService} from "../../service/user.service";
import {BaseChartDirective} from "ng2-charts";

@Component({
  selector: "user-statistics",
  templateUrl: "user-statistics.html"
})
export class UserStatisticsPage {

  private tips: number[] = [];
  private tipsLabel = [];

  @ViewChild(BaseChartDirective)
  public chart: BaseChartDirective;

  constructor(private userService: UserService) {
    this.userService.getLastWeekTips().then((resp) => {
      let data = resp.json();

      for (let key in data) {
        if (data.hasOwnProperty(key)) {
          this.tips.push(Number.parseFloat(data[key].toFixed(2)));
          this.tipsLabel.push(new Date(Number.parseFloat(key)).toLocaleDateString());
        }
      }


      // redraw
      this.chart.ngOnInit();
    })
  }

}
