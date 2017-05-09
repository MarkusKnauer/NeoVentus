import {BrowserModule} from "@angular/platform-browser";
import {ErrorHandler, NgModule} from "@angular/core";
import {IonicApp, IonicErrorHandler, IonicModule} from "ionic-angular";
import {SplashScreen} from "@ionic-native/splash-screen";
import {StatusBar} from "@ionic-native/status-bar";
import {HttpModule} from "@angular/http";
import "rxjs/Rx";

import {MyApp} from "./app.component";
import {LoginPage} from "../pages/login/login";
import {UserService} from "../service/user.service";
import {AuthGuardService} from "../service/auth-guard.service";
import {DeskOverviewPage} from "../pages/desk-overview/desk-overview";
import {DeskService} from "../service/desk.service";
import {DeskPage} from "../pages/desk/desk";
import {OrderService} from "../service/order.service";
import {KitchenOverviewPage} from "../pages/kitchen-overview/kitchen-overview";
import {ShiftsPage} from "../pages/shifts/shifts";
import {MessagePage} from "../pages/messages/messagePage";
import {InvoicesPage} from "../pages/invoices/invoices";
import {SettingsPage} from "../pages/settings/settings";
import {ProfilePage} from "../pages/profile/profile";


@NgModule({
  declarations: [
    MyApp,
    LoginPage,
    DeskOverviewPage,
    DeskPage,
    ShiftsPage,
    MessagePage,
    InvoicesPage,
    SettingsPage,
    ProfilePage,
    KitchenOverviewPage
  ],
  imports: [
    HttpModule,
    BrowserModule,
    IonicModule.forRoot(MyApp, {}, {
      links: [
        // browser support links
        {component: LoginPage, name: "Login", segment: "login"},
        {component: DeskOverviewPage, name: "Desk Overview", segment: "desks"},
        {component: DeskPage, name: "ShowOrders", segment: "orders/{number}"},
        {component: KitchenOverviewPage, name: "Kitchen Overview", segment: "kitchen"}
      ]
    })
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp,
    LoginPage,
    DeskOverviewPage,
    DeskPage,
    ShiftsPage,
    MessagePage,
    InvoicesPage,
    SettingsPage,
    ProfilePage,
    KitchenOverviewPage
  ],
  providers: [
    StatusBar,
    SplashScreen,
    {provide: ErrorHandler, useClass: IonicErrorHandler},
    AuthGuardService,
    UserService,
    DeskService,
    OrderService,
    KitchenOverviewPage
  ]
})
export class AppModule {
}
