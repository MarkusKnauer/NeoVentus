import {Component, Input, ViewChild} from "@angular/core";
import {Events, Nav} from "ionic-angular";
import {DeskOverviewPage} from "../../pages/desk-overview/desk-overview";
import {ShiftsPage} from "../../pages/shifts/shifts";
import {MessagePage} from "../../pages/messages/message";
import {InvoicesPage} from "../../pages/invoices/invoices";
import {KitchenOverviewPage} from "../../pages/kitchen-overview/kitchen-overview";
import {SettingsPage} from "../../pages/settings/settings";
import {ProfilePage} from "../../pages/profile/profile";
import {UserService} from "../../service/user.service";
import {LoginPage} from "../../pages/login/login";
import {AuthGuardService} from "../../service/auth-guard.service";

/**
 * @author Markus Knauer
 * @version 0.0.1 created by MK
 */


@Component({
  templateUrl: "side-menu.html",
  selector: "nv-side-menu"
})
export class SideMenuComponent {

  @ViewChild(Nav) nav: Nav;

  @Input()
  private appContent: any;

  pages: Array<{ title: string, component: any, icon: string }>;
  profilepage: any;
  activepage: any;

  private user = null;

  constructor(private userService: UserService,
              private events: Events,
              private authGuard: AuthGuardService) {

    //todo: edit permissions MK

    if (this.authGuard.isAuthenticated()) {

      if (this.authGuard.hasRole("ROLE_SERVICE")) {

        this.pages = [
          {title: 'Tischübersicht', component: DeskOverviewPage, icon: 'home'},
          {title: 'Schicht', component: ShiftsPage, icon: 'clock'},
          {title: 'Nachrichten', component: MessagePage, icon: 'chatboxes'},
          {title: 'Rechnungen', component: InvoicesPage, icon: 'calculator'},
          {title: 'Einstellungen', component: SettingsPage, icon: 'settings'}
        ];


      } else if (this.authGuard.hasRole("ROLE_CHEF")) {

        this.pages = [
          {title: 'Nachrichten', component: MessagePage, icon: 'chatboxes'},
          {title: 'Küche', component: KitchenOverviewPage, icon: 'bonfire'},
          {title: 'Einstellungen', component: SettingsPage, icon: 'settings'}
        ];

      } else {

        this.pages = [
          {title: 'Tischübersicht', component: DeskOverviewPage, icon: 'home'},
          {title: 'Schicht', component: ShiftsPage, icon: 'clock'},
          {title: 'Nachrichten', component: MessagePage, icon: 'chatboxes'},
          {title: 'Rechnungen', component: InvoicesPage, icon: 'calculator'},
          {title: 'Küche', component: KitchenOverviewPage, icon: 'bonfire'},
          {title: 'Einstellungen', component: SettingsPage, icon: 'settings'}
        ];

      }
    }

    this.profilepage = {title: "Profile", component: ProfilePage};
    this.activepage = this.pages[0];
  }

  openPage(page) {
    // Reset the content nav to have just this page
    this.events.publish('Open-Menu-Page', page.component)
    this.activepage = page;
  }

  logout() {
    this.userService.logout().then(() => {
      this.events.publish('Open-Menu-Page', LoginPage)
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
        console.error("Side menu - Cannot read username");
      }
    }
    return this.user;
  }

}

