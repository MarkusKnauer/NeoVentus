import {Component} from "@angular/core";
import {NavParams, ToastController, ViewController} from "ionic-angular";
import {AuthGuardService} from "../../service/auth-guard.service";
import {BillingDto} from "../../model/billing-dto";
import {BillingService} from "../../service/billing.service";
/**
 * @author Dennis Thanner
 */
@Component({
  selector: "billing-checkout-modal",
  templateUrl: "billing-checkout-modal.html"
})
export class BillingCheckoutModalComponent {

  private ids: Array<string>;
  private sum: number;

  private totalPay: number;

  private customPriceSelected = false;

  constructor(navParams: NavParams, private authGuard: AuthGuardService, private billingService: BillingService,
              private viewCtrl: ViewController, private toastCtrl: ToastController) {
    this.ids = navParams.get("ids");
    this.sum = navParams.get("sum");
    this.totalPay = navParams.get("sum");
  }

  /**
   * return suggestion for total paid after given percentage to add on price
   * round to .0 or 0.5
   *
   * @param perc
   * @returns {number}
   */
  getPaySuggestion(perc: number) {
    let suggestion = this.sum * (1 + (perc / 100));
    let decimal = suggestion % 1;
    suggestion = parseFloat(suggestion.toFixed(0));
    if (decimal >= .025 && decimal < 0.75) {
      // round to 0.5 as decimal
      suggestion += 0.5;
    } else {
      // rounded to 0.0
    }

    return suggestion;
  }

  /**
   * checkout the billing
   */
  checkout() {
    let billing = new BillingDto(this.totalPay, this.authGuard.getUserId(), this.ids);
    this.billingService.insertBilling(billing).then(() => {
      let toast = this.toastCtrl.create({
        duration: 3500,
        showCloseButton: true,
        closeButtonText: "Ok",
        message: "Erfolgreich abgerechnet",
      })
      toast.present();
      this.viewCtrl.dismiss(true);
    })
  }

  /**
   * cancel checkout and
   * close checkout modal
   */
  cancel() {
    this.viewCtrl.dismiss(false);
  }

}
