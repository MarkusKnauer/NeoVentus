import {Component, ViewChild} from "@angular/core";
import {Content, NavController} from "ionic-angular";
import {AuthGuardService} from "../../service/auth-guard.service";

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
  private userroles: any = null;

  private profit_today;
  private profit_all;

  private steps_today;
  private steps_all;

  @ViewChild(Content) content: Content;


  constructor(public navCtrl: NavController, private authGuard: AuthGuardService) {

    // TODO: make avatar dynamic
    this.avatar = "../assets/avatars/default-male.png";

    this.getUserName();
    this.getUserRoles();

    // TODO: get profit data
    this.profit_today = 50.00;
    this.profit_all = 20000.00;

    // TODO: get steps data
    this.steps_today = 500;
    this.steps_all = 200000;
  }

  getUserName() {
    if (this.username == null) {
      try {
        this.username = this.authGuard.userDetails.name;
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


  fixDimensions() {
    this.content.resize()
  }
}
