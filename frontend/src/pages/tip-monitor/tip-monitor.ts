import {Component, ViewChild} from "@angular/core";
import {UserService} from "../../service/user.service";
import {BaseChartDirective} from "ng2-charts";
import {LocalStorageService} from "../../service/local-storage.service";
import {AuthGuardService} from "../../service/auth-guard.service";
import {LoginPage} from "../login/login";
import {NavController} from "ionic-angular";
import {Role} from "../../app/roles";

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

  public monthlyTipGoal: string = "0";

  constructor(private userService: UserService, private localStorageService: LocalStorageService,
              private authGuard: AuthGuardService, private navCtrl: NavController) {
    Promise.all([
      this.userService.getLastWeekTips(),
      this.localStorageService.loadMonthlyTipGoal()
    ])
      .then((resp) => {
        let data = resp[0].json();

        for (let key in data) {

          if (data.hasOwnProperty(key)) {
            this.tips[0].data.push(Number.parseFloat(data[key].toFixed(2)));
            this.tipsLabel.push(new Date(Number.parseFloat(key)).toLocaleDateString());
          }
        }

        this.monthlyTipGoal = this.localStorageService.cache[LocalStorageService.MONTHLY_TIP_GOAL];
        if (this.monthlyTipGoal == null) {
          this.monthlyTipGoal = "0";
        }

        this.updateDailyGoal(true);

        // redraw
        this.chart.ngOnInit();

      })
  }

  /**
   * view life cycle method
   *
   * RBMA
   */
  ionViewWillEnter() {
    this.authGuard.hasAnyRolePromise([Role.SERVICE, Role.BAR]).then(() => {
    }, () => {
      console.debug("RBMA - Access denied!");
      this.navCtrl.setRoot(LoginPage);
    });
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

  updateDailyGoal(noUpdate?: boolean) {
    console.info("Goal change");
    let newGoalData = [];
    for (let i of this.tips[0].data) {
      newGoalData.push(Number.parseFloat((Number.parseFloat(this.monthlyTipGoal) / this.getDaysInCurrentMonth()).toFixed(2)));
    }
    this.tips[1].data = newGoalData;
    if (!noUpdate)
      this.chart.ngOnInit();
  }

  saveDailyGoal() {
    this.localStorageService.saveData(LocalStorageService.MONTHLY_TIP_GOAL, Number.parseFloat(this.monthlyTipGoal).toFixed(2));
  }

}
