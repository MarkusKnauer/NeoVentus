import {Component} from "@angular/core";
import {NavController} from "ionic-angular";

/**
 * @author Markus Knauer
 * @version 0.0.1
 */
@Component({
  selector: 'page-message',
  templateUrl: 'message.html'
})
export class MessagePage {

  public iconName = "home";

  constructor(public navCtrl: NavController) {

  }

}
