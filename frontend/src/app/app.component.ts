import {Component, ViewChild} from "@angular/core";
import {Events, Nav, Platform} from "ionic-angular";
import {StatusBar} from "@ionic-native/status-bar";
import {SplashScreen} from "@ionic-native/splash-screen";
import {LoginPage} from "../pages/login/login";
import {AuthGuardService} from "../service/auth-guard.service";
import {LocalStorageService} from "../service/local-storage.service";
import {ApplicationEvents} from "./events";


/**
 * @author Dennis Thanner, Markus Knauer
 * @version 0.0.4 exported side-menu MK
 *          0.0.3 added userDetail loading on app start
 *          0.0.2 edited side-menu fuction
 *          0.0.1 created by DT
 */
@Component({
  templateUrl: 'app.html'
})
export class MyApp {

  @ViewChild(Nav) nav: Nav;

  rootPage: any = LoginPage;

  public static CONNECTION_URL = "";

  constructor(platform: Platform, statusBar: StatusBar, splashScreen: SplashScreen, authGuard: AuthGuardService, private events: Events,
              localStorageService: LocalStorageService) {
    platform.ready().then(() => {
      // Okay, so the platform is ready and our plugins are available.
      // Here you can do any higher level native things you might need.
      statusBar.styleDefault();
      splashScreen.hide();
    });

    if (platform.is("cordva")) {
      localStorageService.loadConnectionUrl().then(() => {
        MyApp.CONNECTION_URL = localStorageService.cache[LocalStorageService.CONNECTION_URL];
        if (MyApp.CONNECTION_URL == null) {
          // prevent null values ins urls
          MyApp.CONNECTION_URL = "";
        }
        events.publish(ApplicationEvents.CONNECTION_CHANGE_EVENT, MyApp.CONNECTION_URL);

        authGuard.loadUserDetails();
      });
    } else {
      authGuard.loadUserDetails();
    }


    events.subscribe('Open-Menu-Page', (event) => {
      this.nav.setRoot(event.page, event.data);
    })
  }
}

