/**
 * @author Dennis Thanner
 */
import {Component} from "@angular/core";
import {NavParams, ViewController} from "ionic-angular";
import {MenuService} from "../../service/menu.service";
import {OrderService} from "../../service/order.service";
@Component({
  selector: "order-group-detail-modal",
  templateUrl: "order-group-detail-modal.html"
})
export class OrderGroupDetailModalComponent {

  private initData;

  private orders;

  private processingDetail;

  constructor(navParams: NavParams, private menuService: MenuService, private orderService: OrderService,
              private viewCtrl: ViewController) {
    this.initData = {
      orderIds: navParams.get("orderIds"),
      menuId: navParams.get("itemId")
    };

    this.load();

  }

  /**
   * load content detail
   */
  private load(refresher?) {

    Promise.all([
      this.menuService.getProcessingDetails(this.initData.menuId),
      this.orderService.getOrderInfos(this.initData.orderIds)
    ]).then((data) => {
      this.processingDetail = data[0].json();
      this.orders = data[1].json();
      this.orders.sort((a, b) => {
        return b.states[0].date - a.states[0].date;
      });
      for (let order of this.orders) {
        if (order.currentState == "NEW")
          this.getProgress(order);
      }

      if (refresher) {
        refresher.complete();
      }
    });
  }

  /**
   * get progress for order
   */
  public getProgress(order) {
    let start = order.states[0].date;
    let now = new Date().getTime();

    let diffInSeconds = (now - start) / 1000;
    order.progress = diffInSeconds * 100 / this.processingDetail.timeSec;
    let eta = Math.round(this.processingDetail.timeSec - diffInSeconds);
    if (Math.abs(eta) > 60) {
      order.eta = ((Math.abs(eta) / 60).toFixed(2) + " Minuten").replace(".", ",");
    } else {
      order.eta = (Math.abs(eta) + " Sekunden").replace(".", ",");
    }
  }

  /**
   * close modal
   */
  public close() {
    this.viewCtrl.dismiss();
  }

}
