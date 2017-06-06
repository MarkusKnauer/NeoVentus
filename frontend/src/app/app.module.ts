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
import {ManageStornoReasonsModalComponent} from "../component/manage-storno-reasons/manage-storno-reasons";
import {CustomDataPipe} from "../pipe/DatePipe";
import {WorkingShiftService} from "../service/workingShifts.service";
import {SocketService} from "../service/socket.service";
import {NotificationService} from "../service/notification.service";
import {LocalNotifications} from "@ionic-native/local-notifications";
import {BillingModalComponent} from "../component/billing-modal/billing-modal";
import {BillingCheckoutModalComponent} from "../component/billing-checkout-modal/billing-checkout-modal";
import {OrderGroupDetailModalComponent} from "../component/order-group-detail-modal/order-group-detail-modal";
import {ReservationPage} from "../pages/reservation/reservation";
import {IBeacon} from "@ionic-native/ibeacon";
import {BeaconService} from "../service/beacon.service";


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
    ReservationPage,

    // components
    SideMenuComponent,
    OrderSelectModalComponent,
    MenuDetailModalComponent,
    MenuBoxComponent,
    ManageStornoReasonsModalComponent,
    BillingModalComponent,
    BillingCheckoutModalComponent,
    OrderGroupDetailModalComponent,

    //pipes
    CostDecimalPipe,
    MapToIterable,
    CustomDataPipe,


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
        {component: KitchenOverviewPage, name: "KitchenOverview", segment: "kitchen/:forKitchen"},
        {component: SettingsPage, name: "Settings", segment: "settings"},
        {component: ProfilePage, name: "Profile", segment: "profile"},
        {component: InvoicesPage, name: "billings", segment: "billlings"}
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
    ReservationPage,
    OrderSelectModalComponent,
    MenuDetailModalComponent,
    ManageStornoReasonsModalComponent,
    BillingModalComponent,
    BillingCheckoutModalComponent,
    OrderGroupDetailModalComponent
  ],
  providers: [
    StatusBar,
    SplashScreen,
    LocalNotifications,
    IBeacon,
    {provide: ErrorHandler, useClass: IonicErrorHandler},
    StompService,
    SocketService,
    NotificationService,
    LocalStorageService,
    AuthGuardService,
    UserService,
    DeskService,
    OrderService,
    MenuCategoryService,
    MenuService,
    OrderSocketService,
    ReservationService,
    BillingService,
    WorkingShiftService,
    BeaconService
  ]
})
export class AppModule {
}
