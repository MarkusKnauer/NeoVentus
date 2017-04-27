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

  public login() {
    this.userService.login(this.username, this.password).then(() => {
      // redirect to desk overview
      this.navCtrl.push(DeskOverviewPage);
    }).catch(() => {
      console.debug("Failed to login");
    })
  }

}
