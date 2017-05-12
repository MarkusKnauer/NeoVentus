import {Component} from "@angular/core";
import {ViewController} from "ionic-angular";
/**
 * @author Dennis Thanner
 * @version 0.0.1
 */

@Component({
  selector: "menu-detail-modal",
  templateUrl: "menu-detail-modal.html"
})
export class MenuDetailModalComponent {


  constructor(private viewCtrl: ViewController) {
    console.debug(viewCtrl);
  }

  /**
   * close this modal
   */
  dismiss() {
    this.viewCtrl.dismiss();
  }

}
