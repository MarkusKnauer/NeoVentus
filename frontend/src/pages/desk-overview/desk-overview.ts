import {Component} from "@angular/core";
import {NavController} from "ionic-angular";
import {DeskService} from "../../app/service/desk.service";

/**
 * @author Tim Heidelbach
 * @version 0.0.1
 */
@Component({
  templateUrl: "desk-overview.html",
  providers: [DeskService]
})
export class DeskOverviewPage {

  public desks: any;

  constructor(private navCtrl: NavController, private deskService: DeskService) {
    this.loadDesks();
  }

  loadDesks() {
    this.deskService.getAllDesks()
      .then(
        data => {
          this.desks = data;
        }
      )
  }

  deskSelected(desk) {
    alert("you clicked desk " + desk.number);
  }

  toggleView() {
    // TODO: toggle between grid and list view
    alert("ein sehr mächtiger Button");
  }

}
