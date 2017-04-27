import {BrowserModule} from '@angular/platform-browser';
import {ErrorHandler, NgModule} from '@angular/core';
import {IonicApp, IonicErrorHandler, IonicModule} from 'ionic-angular';
import {SplashScreen} from '@ionic-native/splash-screen';
import {StatusBar} from '@ionic-native/status-bar';
import {HttpModule} from '@angular/http';
import 'rxjs/Rx';

import {MyApp} from './app.component';
import {LoginPage} from '../pages/login/login';
import {UserService} from './service/user.service';
import {AuthGuardService} from './service/auth-guard.service';
import {DeskOverviewPage} from '../pages/desk-overview/desk-overview';

@NgModule({
  declarations: [
    MyApp,
    LoginPage,
    DeskOverviewPage
  ],
  imports: [
    HttpModule,
    BrowserModule,
    IonicModule.forRoot(MyApp)
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp,
    LoginPage,
    DeskOverviewPage
  ],
  providers: [
    StatusBar,
    SplashScreen,
    {provide: ErrorHandler, useClass: IonicErrorHandler},
    AuthGuardService,
    UserService
  ]
})
export class AppModule {
}
