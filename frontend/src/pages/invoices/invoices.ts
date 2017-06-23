import {Component} from "@angular/core";
import {NavController} from "ionic-angular";
import {BillingService} from "../../service/billing.service";
import {LoginPage} from "../login/login";
import {AuthGuardService} from "../../service/auth-guard.service";
import {Role} from "../../app/roles";

/**
 * @author Markus Knauer
 * @author Dennis Thanner
 */

@Component({
  selector: 'page-invoice',
  templateUrl: 'invoices.html'
})
export class InvoicesPage {

  constructor(public navCtrl: NavController, private billingService: BillingService,
              private authGuard: AuthGuardService) {
    this.load();
  }

  load(refresher?) {
    this.billingService.getTodaysBillings().then(() => {
      if (refresher)
        refresher.complete();
    })
  }

  /**
   * view life cycle method
   *
   * RBMA
   */
  ionViewWillEnter() {
    this.authGuard.hasAnyRolePromise([Role.SERVICE, Role.CEO]).then(() => {
    }, () => {
      console.debug("RBMA - Access denied!");
      this.navCtrl.setRoot(LoginPage);
    });
  }

}
