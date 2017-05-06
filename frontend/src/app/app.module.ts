import {BrowserModule} from "@angular/platform-browser";
import {ErrorHandler, NgModule} from "@angular/core";
import {IonicApp, IonicErrorHandler, IonicModule} from "ionic-angular";
import {SplashScreen} from "@ionic-native/splash-screen";
import {StatusBar} from "@ionic-native/status-bar";
import {HttpModule} from "@angular/http";
import "rxjs/Rx";

import {MyApp} from "./app.component";
import {LoginPage} from "../pages/login/login";
import {UserService} from "./service/user.service";
import {AuthGuardService} from "./service/auth-guard.service";
import {DeskOverviewPage} from "../pages/desk-overview/desk-overview";
import {DeskService} from "./service/desk.service";
import {ShowOrdersPage} from "../pages/showOrders/showOrders";
import {ShowOrdersService} from "./service/showOrders.service";
import {DeskPage} from "../pages/desk/desk";
import {ShiftsPage} from "../pages/shiftsPage/shiftsPage";
import {MessagePage} from "../pages/messagePage/messagePage";
import {Invoices} from "../pages/invoices/invoices";
import {Settings} from "../pages/settings/settings";


@NgModule({
  declarations: [
    MyApp,
    LoginPage,
    DeskOverviewPage,
    DeskPage,
    DeskOverviewPage,
    ShowOrdersPage,
    ShiftsPage,
    MessagePage,
    Invoices,
    Settings
  ],
  imports: [
    HttpModule,
    BrowserModule,
    IonicModule.forRoot(MyApp, {}, {
      links: [
        // browser support links
        {component: LoginPage, name: "Login", segment: "login"},
        {component: DeskOverviewPage, name: "Desk Overview", segment: "desks"},
        {component: ShowOrdersPage, name: "ShowOrders", segment: "orders"}
      ]
    })
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp,
    LoginPage,
    DeskOverviewPage,
    DeskPage,
    DeskOverviewPage,
    ShowOrdersPage,
    ShiftsPage,
    MessagePage,
    Invoices,
    Settings
  ],
  providers: [
    StatusBar,
    SplashScreen,
    {provide: ErrorHandler, useClass: IonicErrorHandler},
    AuthGuardService,
    UserService,
    DeskService,
    ShowOrdersService
  ]
})
export class AppModule {
}
