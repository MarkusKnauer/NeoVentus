import {Component} from "@angular/core";
import {AuthGuardService} from "../../service/auth-guard.service";
import {NavController} from "ionic-angular";
import {Role} from "../../app/roles";
import {LoginPage} from "../login/login";
import {BillingService} from "../../service/billing.service";
import {OrderService} from "../../service/order.service";

@Component({
  selector: "stats",
  templateUrl: "stats.html"
})
export class StatsPage {

  public quarters = {
    data: [{data: [0], label: "Umsatz"}],
    labels: [""]
  };

  public category = {
    data: [1, 0, 0, 0],
    labels: ["Test", "Test1", "Test2", "Test3"]
  };

  public topMenusKitchen = [];
  public topMenusBar = [];

  public topWaiters = [];

  constructor(private authGaurd: AuthGuardService, private navCtrl: NavController,
              private billingService: BillingService, private orderService: OrderService) {
    this.loadAll()
  }

  /**
   * load all data
   *
   * @param refresher
   */
  loadAll(refresher?) {
    this.loadQuartersRevenue();
    this.loadCategoryDistribution();
    this.orderService.getTop10MenuItemsThisMonth(true).then((resp) => {
      this.topMenusKitchen = resp.json();
    });
    this.orderService.getTop10MenuItemsThisMonth(false).then((resp) => {
      this.topMenusBar = resp.json();
    });
    this.billingService.getTop10Waiters().then((resp) => {
      this.topWaiters = resp.json();
    });
    if (refresher)
      refresher.complete();
  }

  /**
   * load quarters revenue
   */
  loadQuartersRevenue() {
    // console.debug(this.quarterChart);
    this.billingService.getQuartersRevenue().then((resp) => {
      let _labels = [];
      let _data = [];
      for (let q of resp.json()) {
        _data.push(q.revenue);
        _labels.push("Q" + q.object);
      }
      this.quarters.data[0].data = _data;
      this.quarters.labels = _labels;
    });
  }

  /**
   * load category distribution
   */
  loadCategoryDistribution() {
    this.orderService.getCategoryDistributionThisMonth().then((resp) => {
      let _data = [];
      let _labels = [];
      for (let c of resp.json()) {
        _data.push(c.count);
        _labels.push(c.category);
      }

      this.category.labels = _labels;
      setTimeout(() => {
        this.category.data = _data;
      }, 10)

    })
  }

  /**
   * role base access management
   */
  ionViewWillEnter() {
    this.authGaurd.hasRolePromise(Role.CEO).then(() => {
      // granted
    }, () => {
      if (this.navCtrl.length() > 1)
        this.navCtrl.pop();
      else
        this.navCtrl.setRoot(LoginPage)
    })
  }

}
