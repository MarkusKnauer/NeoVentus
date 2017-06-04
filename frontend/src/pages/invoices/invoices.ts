import {Component} from "@angular/core";
import {NavController} from "ionic-angular";
import {BillingService} from "../../service/billing.service";

/**
 * @author Markus Knauer
 * @author Dennis Thanner
 */

@Component({
  selector: 'page-invoice',
  templateUrl: 'invoices.html'
})
export class InvoicesPage {

  constructor(public navCtrl: NavController, private billingService: BillingService) {
    this.load();
  }

  load(refresher?) {
    this.billingService.getTodaysBillings().then(() => {
      if (refresher)
        refresher.complete();
    })
  }

}
