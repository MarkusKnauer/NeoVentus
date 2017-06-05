import {Component, Input, ViewChild} from "@angular/core";
import {Events, Nav} from "ionic-angular";
import {DeskOverviewPage} from "../../pages/desk-overview/desk-overview";
import {ShiftsPage} from "../../pages/shifts/shifts";
import {MessagePage} from "../../pages/messages/message";
import {InvoicesPage} from "../../pages/invoices/invoices";
import {KitchenOverviewPage} from "../../pages/kitchen-overview/kitchen-overview";
import {ReservationPage} from "../../pages/reservation/reservation";
import {SettingsPage} from "../../pages/settings/settings";
import {ProfilePage} from "../../pages/profile/profile";
import {UserService} from "../../service/user.service";
import {LoginPage} from "../../pages/login/login";
import {AuthGuardService} from "../../service/auth-guard.service";

/**
 * @author Markus Knauer
 */
@Component({
  templateUrl: "side-menu.html",
  selector: "nv-side-menu"
})
export class SideMenuComponent {

  @ViewChild(Nav) nav: Nav;

  @Input()
  private appContent: any;

  pages: Array<any>;
  profilepage: any;
  activepage: any;

  private user = null;
  private userroles: any = null;

  constructor(private userService: UserService,
              private events: Events,
              private authGuard: AuthGuardService) {

    //todo: edit permissions MK

    this.getUserName();
    this.getUserRoles();

        this.pages = [
          {title: 'Tischübersicht', component: DeskOverviewPage, icon: 'home', roles: ["ROLE_CEO", "ROLE_SERVICE"]},
          {title: 'Schicht', component: ShiftsPage, icon: 'clock', roles: ["ROLE_CEO", "ROLE_SERVICE"]},
          {
            title: 'Nachrichten',
            component: MessagePage,
            icon: 'chatboxes',
            roles: ["ROLE_CEO", "ROLE_SERVICE", "ROLE_CHEF"]
          },
          {title: 'Rechnungen', component: InvoicesPage, icon: 'calculator', roles: ["ROLE_CEO", "ROLE_SERVICE"]},
          {
            title: 'Küche',
            component: KitchenOverviewPage,
            icon: 'bonfire',
            roles: ["ROLE_CEO", "ROLE_CHEF"],
            data: {forKitchen: 1}
          },
          {
            title: 'Bar',
            component: KitchenOverviewPage,
            icon: 'beer',
            roles: ["ROLE_CEO", "ROLE_BAR"],
            data: {forKitchen: 0}
          },
          {
            title: 'Reservierungen',
            component: ReservationPage,
            icon: 'clipboard',
            roles: ["ROLE_CEO", "ROLE_BAR"]
          },
          {
            title: 'Einstellungen',
            component: SettingsPage,
            icon: 'settings',
            roles: ["ROLE_SERVICE", "ROLE_CEO", "ROLE_CHEF"]
          }
        ];


    this.profilepage = {title: "Profile", component: ProfilePage};
    this.activepage = this.pages[0];
  }

  openPage(page, data) {
    // Reset the content nav to have just this page
    this.events.publish('Open-Menu-Page', {page: page.component, data});
    this.activepage = page;
  }

  logout() {
    this.userService.logout().then(() => {
      this.events.publish('Open-Menu-Page', {page: LoginPage});
      this.user = null;
    }, (err) => {
      console.debug(err)
    })
  }

  checkActive(page) {
    return page == this.activepage;
  }

  getUserName() {
    if (this.user == null) {
      try {
        this.user = this.authGuard.userDetails.name;
      } catch (exception) {
      }
    }
    return this.user;
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

}

