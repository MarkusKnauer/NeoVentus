import {Component} from "@angular/core";
import {NavParams, ViewController} from "ionic-angular";
import {MenuService} from "../../service/menu.service";
import {LocalStorageService} from "../../service/local-storage.service";
import {Order} from "../../model/order";
/**
 * @author Dennis Thanner
 * @version 0.0.3 added fav and return data on exit - DT
 *          0.0.2 added side dish selection - DT
 */

@Component({
  selector: "menu-detail-modal",
  templateUrl: "menu-detail-modal.html"
})
export class MenuDetailModalComponent {

  private menu;

  private selectedSideDish = {};

  private wish: string = "";

  constructor(private viewCtrl: ViewController, private navParams: NavParams, private menuService: MenuService,
              private localStorageService: LocalStorageService) {
    this.menu = this.menuService.cache["all"].find(el => {
      return el.id == navParams.data.id;
    });
    if (this.menu.sideDishGroup) {
      for (let sideDish of this.menu.sideDishGroup.sideDishes) {
        this.selectedSideDish[sideDish.id] = false;
      }
    }
  }

  /**
   * is menu Favorite
   * @returns {boolean}
   */
  isFavorite() {
    if (!this.localStorageService.cache["favs"]) {
      return false;
    }
    return this.localStorageService.cache["favs"].indexOf(this.menu.id) != -1;
  }

  /**
   * cancel and
   * close this modal
   */
  cancel() {
    this.viewCtrl.dismiss();
  }

  /**
   * successfully close modal
   * return data
   */
  add() {
    let sideDishes = [];
    if (this.menu.sideDishGroup) {
      for (let sideDish of this.menu.sideDishGroup.sideDishes) {
        if (this.selectedSideDish[sideDish.id]) {
          sideDishes.push(sideDish);
        }
      }
      if (this.menu.sideDishGroup.isSelectionRequired && sideDishes.length == 0) {

        alert("Bitte treffen Sie ihre Auswahl!" + this.menu.sideDishGroup.isSelectionRequired);

      } else {

        alert("Keine Auswahl gewünscht");
        this.viewCtrl.dismiss(new Order(this.menu, sideDishes, this.wish));
      }
    }
  }

}
