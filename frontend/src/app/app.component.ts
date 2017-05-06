import {Component, ViewChild} from "@angular/core";
import {Nav, Platform} from "ionic-angular";
import {StatusBar} from "@ionic-native/status-bar";
import {SplashScreen} from "@ionic-native/splash-screen";

import {LoginPage} from "../pages/login/login";
import {ShiftsPage} from "../pages/shiftsPage/shiftsPage";
import {MessagePage} from "../pages/messagePage/messagePage";
import {Invoices} from "../pages/invoices/invoices";
import {Settings} from "../pages/settings/settings";
import {DeskOverviewPage} from "../pages/desk-overview/desk-overview";


@Component({
  templateUrl: 'app.html'
})
export class MyApp {
  @ViewChild(Nav) nav: Nav;
  rootPage: any = LoginPage;

  pages: Array<{ title: string, component: any }>;

  constructor(platform: Platform, statusBar: StatusBar, splashScreen: SplashScreen) {
    platform.ready().then(() => {
      // Okay, so the platform is ready and our plugins are available.
      // Here you can do any higher level native things you might need.
      statusBar.styleDefault();
      splashScreen.hide();
    });
    //todo: Link the pages (MK):
    this.pages = [
      {title: 'Home', component: DeskOverviewPage},
      {title: 'Schicht', component: ShiftsPage},
      {title: 'Nachrichten', component: MessagePage},
      {title: 'Rechnungen', component: Invoices},
      {title: 'Einstellungen', component: Settings},
    ];
  }

  openPage(page) {
    // Reset the content nav to have just this page
    this.nav.setRoot(page.component);


  }


}

