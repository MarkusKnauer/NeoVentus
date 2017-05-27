import {Component, ViewChild} from "@angular/core";
import {Content, NavController} from "ionic-angular";
import {AuthGuardService} from "../../service/auth-guard.service";
import {UserService} from "../../service/user.service";

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
  private revenue = 0;
  private tips = 0;
  private level = 1;
  private xp = 31154;
  private expNextLevel = 100000;
  private steps = 0;

  private today: Date;

  @ViewChild(Content) content: Content;

  constructor(public navCtrl: NavController,
              private authGuard: AuthGuardService,
              private userService: UserService) {

    // TODO: make avatar dynamic
    this.avatar = "../assets/avatars/default-male.png";

    this.today = new Date();

    this.getUserName();
    this.getUserRoles();
    this.getUserProfile();

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

  getUserProfile() {
    this.userService.getProfileDetails().then(
      profile => {
        this.xp = profile.exp;
        this.level = profile.level;
        this.expNextLevel = profile.expNextLevel;
        this.revenue = profile.revenueToday;
        this.tips = profile.tipsToday;
      }
    )
  }

  fixDimensions() {
    this.content.resize()
  }
}
