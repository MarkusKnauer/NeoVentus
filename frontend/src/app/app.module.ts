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
import {MessagePage} from "../pages/messages/message";
import {InvoicesPage} from "../pages/invoices/invoices";
import {SettingsPage} from "../pages/settings/settings";
import {ProfilePage} from "../pages/profile/profile";
import {SideMenuComponent} from "../component/side-menu/side-menu";
import {OrderSelectModalComponent} from "../component/order-select-modal/order-select-modal";
import {MenuCategoryService} from "../service/menu-category.service";
import {MenuService} from "../service/menu.service";
import {CostDecimalPipe} from "../pipe/CostDecimalPipe";
import {MapToIterable} from "../pipe/MapToIterable";
import {MenuDetailModalComponent} from "../component/menu-detail-modal/menu-detail-modal";
import {MenuBoxComponent} from "../component/menu-box/menu-box";
import {IonicStorageModule} from "@ionic/storage";
import {LocalStorageService} from "../service/local-storage.service";
import {OrderSocketService} from "../service/order-socket-service";
import {StompService} from "ng2-stomp-service";
import {ReservationService} from "../service/reservation.service";
import {BillingService} from "../service/billing.service";


@NgModule({
  declarations: [
    MyApp,

    // pages
    LoginPage,
    DeskOverviewPage,
    DeskPage,
    ShiftsPage,
    MessagePage,
    InvoicesPage,
    SettingsPage,
    ProfilePage,
    KitchenOverviewPage,

    // components
    SideMenuComponent,
    OrderSelectModalComponent,
    MenuDetailModalComponent,
    MenuBoxComponent,

    //pipes
    CostDecimalPipe,
    MapToIterable

  ],
  imports: [
    HttpModule,
    BrowserModule,
    IonicModule.forRoot(MyApp, {}, {
      links: [
        // browser support links
        {component: LoginPage, name: "Login", segment: "login"},
        {component: DeskOverviewPage, name: "DeskOverview", segment: "desks"},
        {component: DeskPage, name: "ShowOrders", segment: "orders/:deskNumber", defaultHistory: ["DeskOverview"]},
        {component: KitchenOverviewPage, name: "KitchenOverview", segment: "kitchen/:forKitchen"}
      ]
    }),
    IonicStorageModule.forRoot()
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
    KitchenOverviewPage,
    OrderSelectModalComponent,
    MenuDetailModalComponent
  ],
  providers: [
    StatusBar,
    SplashScreen,
    {provide: ErrorHandler, useClass: IonicErrorHandler},
    StompService,
    AuthGuardService,
    UserService,
    DeskService,
    OrderService,
    MenuCategoryService,
    MenuService,
    LocalStorageService,
    OrderSocketService,
    ReservationService,
    BillingService
  ]
})
export class AppModule {
}
