import {Component, Input, ViewChild} from "@angular/core";
import {Events, Nav} from "ionic-angular";
import {DeskOverviewPage} from "../../pages/desk-overview/desk-overview";
import {ShiftsPage} from "../../pages/shifts/shifts";
import {MessagePage} from "../../pages/messages/messagePage";
import {InvoicesPage} from "../../pages/invoices/invoices";
import {KitchenOverviewPage} from "../../pages/kitchen-overview/kitchen-overview";
import {SettingsPage} from "../../pages/settings/settings";
import {ProfilePage} from "../../pages/profile/profile";
import {UserService} from "../../service/user.service";
import {LoginPage} from "../../pages/login/login";

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

  constructor(private userService: UserService, private events: Events) {

    //todo: write logout fuction (MK)
    this.pages = [
      {title: 'Home', component: DeskOverviewPage, icon: 'home'},
      {title: 'Schicht', component: ShiftsPage, icon: 'clock'},
      {title: 'Nachrichten', component: MessagePage, icon: 'chatboxes'},
      {title: 'Rechnungen', component: InvoicesPage, icon: 'calculator'},
      {title: 'Küche', component: KitchenOverviewPage, icon: 'bonfire'},
      {title: 'Einstellungen', component: SettingsPage, icon: 'settings'}
    ];


    this.profilepage = {title: "Profile", component: ProfilePage};
  }

  openPage(page) {
    // Reset the content nav to have just this page
    this.events.publish('Open-Menu-Page', page.component)
  }

  logout() {
    this.userService.logout().toPromise().then(() => {
      this.events.publish('Open-Menu-Page', LoginPage)
    }, (err) => {
      console.debug(err)
    })
  }


}

