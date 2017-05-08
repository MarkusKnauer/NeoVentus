import {Component, ViewChild} from "@angular/core";
import {Nav, Platform} from "ionic-angular";
import {StatusBar} from "@ionic-native/status-bar";
import {SplashScreen} from "@ionic-native/splash-screen";

import {LoginPage} from "../pages/login/login";
import {ShiftsPage} from "../pages/shiftsPage/shiftsPage";
import {MessagePage} from "../pages/messagePage/messagePage";
import {Invoices} from "../pages/invoices/invoices";
import {Settings} from "../pages/settings/settings";
import {KitchenOverviewPage} from "../pages/kitchen-overview/kitchen-overview"
import {DeskOverviewPage} from "../pages/desk-overview/desk-overview";
import {ProfilePage} from "../pages/profile/profile";
import {AuthGuardService} from "./service/auth-guard.service";

/**
 * @author Dennis Thanner, Markus Knauer
 * @version 0.0.3 added userDetail loading on app start
 *          0.0.2 edited side-menu fuction
 *          0.0.1 created by DT
 */
@Component({
  templateUrl: 'app.html'
})
export class MyApp {
  @ViewChild(Nav) nav: Nav;
  rootPage: any = LoginPage;

  pages: Array<{ title: string, component: any }>;
  profilepage: any;

  constructor(platform: Platform, statusBar: StatusBar, splashScreen: SplashScreen, authGuard: AuthGuardService) {
    platform.ready().then(() => {
      // Okay, so the platform is ready and our plugins are available.
      // Here you can do any higher level native things you might need.
      statusBar.styleDefault();
      splashScreen.hide();
    });
    //todo: write logout fuction (MK)
    this.pages = [
      {title: 'Home', component: DeskOverviewPage},
      {title: 'Schicht', component: ShiftsPage},
      {title: 'Nachrichten', component: MessagePage},
      {title: 'Rechnungen', component: Invoices},
      {title: 'Küche', component: KitchenOverviewPage},
      {title: 'Einstellungen', component: Settings}
    ];

    authGuard.loadUserDetails();

    this.profilepage = {title: "Profile", component: ProfilePage};
  }

  openPage(page) {
    // Reset the content nav to have just this page
    this.nav.setRoot(page.component);


  }


}

