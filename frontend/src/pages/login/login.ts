import {Component} from '@angular/core';
import {NavController} from 'ionic-angular';
import {UserService} from '../../app/service/user.service';

@Component({
  selector: 'page-home',
  templateUrl: 'login.html'
})
export class LoginPage {

  private username: string;

  private password: string;

  constructor(public navCtrl: NavController, private userService: UserService) {

  }

  public login() {
    this.userService.login(this.username, this.password);
  }

}
