import {Component} from "@angular/core";
import {ViewController} from "ionic-angular";
import {MenuCategoryService} from "../../service/menu-category.service";

/**
 * @author Dennis Thanner
 * @version 0.0.1
 */
@Component({
  templateUrl: "order-select-modal.html"
})
export class OrderSelectModalComponent {

  constructor(private menuCategoryService: MenuCategoryService, private viewCtrl: ViewController) {
    if (!menuCategoryService.cache["tree"]) {
      menuCategoryService.loadCategoryTree();
    }
  }


  /**
   * close modal
   */
  dismiss() {
    this.viewCtrl.dismiss();
  }


}
