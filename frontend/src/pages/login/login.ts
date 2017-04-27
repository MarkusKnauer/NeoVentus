import {Component} from '@angular/core';
import {NavController} from 'ionic-angular';
import {UserService} from '../../app/service/user.service';
import {DeskOverviewPage} from '../desk-overview/desk-overview';

/**
 * @author Dennis Thanner
 * @version 0.0.2 added redirect to deskoverview
 */
@Component({
  selector: 'page-login',
  templateUrl: 'login.html'
})
export class LoginPage {

  private username: string;

  private password: string;

  constructor(public navCtrl: NavController, private userService: UserService) {

  }

  /**
   * prevent login in screen to show up in browser if user is authenticated
   */
  ionViewWillEnter() {
    this.navCtrl.setRoot(DeskOverviewPage)
  }

  /**
   * login user
   * redirect to DeskOverviewPage if successful
   */
  public login() {
    this.userService.login(this.username, this.password).then(() => {
      // redirect to desk overview
      this.navCtrl.setRoot(DeskOverviewPage)
    }).catch(() => {
      console.debug("Failed to login");
    })
  }

}
