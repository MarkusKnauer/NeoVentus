import {Component} from "@angular/core";
import {ModalController, NavController} from "ionic-angular";
import {ManageStornoReasonsModalComponent} from "../../component/manage-storno-reasons/manage-storno-reasons";

/**
 * @author Markus Knauer, Dennis Thanner
 */

@Component({
  selector: 'settings-page',
  templateUrl: 'settings.html'
})
export class SettingsPage {

  constructor(public navCtrl: NavController, private modalCtrl: ModalController) {
  }

  openStornoReasonsModal() {
    this.modalCtrl.create(ManageStornoReasonsModalComponent).present();
  }

}
