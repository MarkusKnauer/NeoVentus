import {Component} from "@angular/core";
import {NavParams, ViewController} from "ionic-angular";
import {MenuService} from "../../service/menu.service";
import {LocalStorageService} from "../../service/local-storage.service";
/**
 * @author Dennis Thanner
 * @version 0.0.2 added side dish selection
 */

@Component({
  selector: "menu-detail-modal",
  templateUrl: "menu-detail-modal.html"
})
export class MenuDetailModalComponent {

  private menu;

  private selectedSideDish = {};

  constructor(private viewCtrl: ViewController, private navParams: NavParams, private menuService: MenuService,
              private _localStorageService: LocalStorageService) {
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
   * close this modal
   */
  dismiss() {
    this.viewCtrl.dismiss();
  }

  // getter setter

  get localStorageService(): LocalStorageService {
    return this._localStorageService;
  }

  set localStorageService(value: LocalStorageService) {
    this._localStorageService = value;
  }
}
