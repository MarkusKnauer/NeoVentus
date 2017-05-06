/**
 * Created by Tim on 04.05.2017.
 */

import {Component} from "@angular/core";
import {NavController, NavParams, ViewController} from "ionic-angular";
/**
 * @author Tim Heidelbach
 * @version 0.0.0
 */
@Component({
  templateUrl: "desk.html",
})
export class DeskPage {

  private number;

  constructor(private navCtrl: NavController, public viewCtrl: ViewController, public params: NavParams) {
    this.number = this.params.get('number');
  }

  dismiss() {
    this.viewCtrl.dismiss();
  }
}
