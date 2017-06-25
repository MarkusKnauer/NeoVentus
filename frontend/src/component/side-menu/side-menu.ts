import {Component, Input, ViewChild} from "@angular/core";
import {Events, Nav} from "ionic-angular";
import {DeskOverviewPage} from "../../pages/desk-overview/desk-overview";
import {ShiftsPage} from "../../pages/shifts/shifts";
import {InvoicesPage} from "../../pages/invoices/invoices";
import {KitchenOverviewPage} from "../../pages/kitchen-overview/kitchen-overview";
import {ReservationPage} from "../../pages/reservation/reservation";
import {SettingsPage} from "../../pages/settings/settings";
import {ProfilePage} from "../../pages/profile/profile";
import {UserService} from "../../service/user.service";
import {LoginPage} from "../../pages/login/login";
import {AuthGuardService} from "../../service/auth-guard.service";
import {Role} from "../../app/roles";
import {StatsPage} from "../../pages/stats/stats";

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

  private settingsPage = {
    title: 'Einstellungen',
    component: SettingsPage,
    icon: 'settings',
  };

  private loginPage = {
    component: LoginPage,
  };

  private user = null;
  private userroles: any = null;

  constructor(private userService: UserService,
              private events: Events,
              private authGuard: AuthGuardService) {

    //todo: edit permissions MK

    this.getUserName();
    this.getUserRoles();

    this.profilepage = {
      title: "Profil",
      component: ProfilePage,
      icon: "person",
      roles: [Role.CEO, Role.SERVICE, Role.CHEF, Role.BAR]
    };

    this.pages = [
      {
        title: 'Tischübersicht',
        component: DeskOverviewPage,
        icon: 'home',
        roles: [Role.CEO, Role.SERVICE]
      },
      {
        title: 'Schicht',
        component: ShiftsPage,
        icon: 'clock',
        roles: [Role.CEO, Role.SERVICE]
      },
      {
        title: 'Rechnungen',
        component: InvoicesPage,
        icon: 'calculator',
        roles: [Role.CEO, Role.SERVICE]
      },
      {
        title: 'Küche',
        component: KitchenOverviewPage,
        icon: 'bonfire',
        roles: [Role.CEO, Role.CHEF],
        data: {forKitchen: 1}
      },
      {
        title: 'Bar',
        component: KitchenOverviewPage,
        icon: 'beer',
        roles: [Role.CEO, Role.BAR],
        data: {forKitchen: 0}
      },
      {
        title: 'Reservierungen',
        component: ReservationPage,
        icon: 'clipboard',
        roles: [Role.CEO, Role.BAR, Role.SERVICE]
      },
      this.profilepage,
      {
        title: "Statistiken",
        component: StatsPage,
        icon: "trending-up",
        roles: [Role.CEO]
      }
    ];

    this.activepage = this.pages[0];
  }

  openPage(page, data?) {
    // Reset the content nav to have just this page
    this.events.publish('Open-Menu-Page', {page: page.component, data});
    this.activepage = page;
  }

  logout() {
    this.userService.logout().then(() => {
      this.events.publish('Open-Menu-Page', {page: LoginPage});
      this.user = null;
      this.activepage = this.pages[0];
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

