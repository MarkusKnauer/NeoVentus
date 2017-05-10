import {Component} from "@angular/core";
import {ViewController} from "ionic-angular";
import {MenuCategoryService} from "../../service/menu-category.service";
import {MenuService} from "../../service/menu.service";

/**
 * @author Dennis Thanner
 * @version 0.0.2 added grouping after loading
 */
@Component({
  templateUrl: "order-select-modal.html",
  selector: "order-select-modal"
})
export class OrderSelectModalComponent {

  constructor(private viewCtrl: ViewController, private menuCategoryService: MenuCategoryService, private menuService: MenuService) {
    if (!menuCategoryService.cache["tree"] || !menuService.cache["all"]) {
      Promise.all([
        menuCategoryService.loadCategoryTree(),
        menuService.getAll()
      ]).then(() => {
        this.groupMenuToCategories();
      }, console.debug)
    }
  }

  /**
   * group menu items to menu categories
   */
  groupMenuToCategories() {
    console.debug("Group menus to categories");
    for (let cat of this.menuCategoryService.cache["tree"]) {
      this.findMenuItems(cat);
    }
    console.debug("finished grouping");
  }

  /**
   * search menus to category recursively
   * @param cat
   */
  findMenuItems(cat) {
    cat["items"] = this.menuService.cache["all"].filter(elem => {
      return elem.menuItemCategory.id == cat.id;
    });
    for (let child of cat.subcategory) {
      this.findMenuItems(child);
    }
  }

  /**
   * close modal
   */
  dismiss() {
    this.viewCtrl.dismiss();
  }


}
