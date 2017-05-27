import {Component, ViewChild} from "@angular/core";
import {Content, NavController} from "ionic-angular";
import {AuthGuardService} from "../../service/auth-guard.service";
import {BillingService} from "../../service/billing.service";

/**
 * @author Markus Knauer, Tim Heidelbach
 * @version 0.0.2 designed profile page
 * @version 0.0.1
 */

@Component({
  selector: 'page-profile',
  templateUrl: 'profile.html'
})
export class ProfilePage {

  private avatar;
  private username = null;
  private userId;
  private userroles: any = null;
  private telephone;
  private email;
  private sales = 0;
  private gratitude = 0;
  private level = 1;
  private xp = 31154;
  private xpToLvlUp = 100000;
  private steps = 0;

  private billings: any;

  private today: Date;

  @ViewChild(Content) content: Content;

  constructor(public navCtrl: NavController,
              private authGuard: AuthGuardService,
              private billingService: BillingService) {

    // TODO: make avatar dynamic
    this.avatar = "../assets/avatars/default-male.png";

    this.today = new Date();

    this.getUserName();
    this.getUserRoles();
    this.getSalesData();

    this.email = this.username.toLowerCase() + "@neovent.us";
  }

  getUserName() {
    if (this.username == null) {
      try {
        this.username = this.authGuard.userDetails.name;
        this.userId = this.authGuard.userDetails.principal.userId;
      } catch (exception) {
        console.error("Profile - Cannot read username");
      }
    }
    return this.username;
  }

  getUserRoles() {
    if (this.userroles == null) {
      try {
        this.userroles = this.authGuard.userDetails.authorities;

      } catch (exception) {
        console.error("Profile - Cannot read userroles");
      }
    }
    return this.userroles;
  }

  getSalesData() {

    this.billingService.getBillingByWaiter(this.userId).then(
      billings => {
        this.billings = billings;

        for (let billing of billings) {

          let date = new Date(billing.billedAt);

          let itemsPrice = 0;
          for (let item of billing.items) {
            itemsPrice += item.price;
          }

          this.sales += itemsPrice;
          this.gratitude += (billing.totalPaid - itemsPrice);
        }
      });
  }

  fixDimensions() {
    this.content.resize()
  }
}
