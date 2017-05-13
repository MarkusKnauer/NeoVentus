import {Component} from "@angular/core";
import {ModalController, ViewController} from "ionic-angular";
import {MenuCategoryService} from "../../service/menu-category.service";
import {MenuService} from "../../service/menu.service";
import {MenuDetailModalComponent} from "../menu-detail-modal/menu-detail-modal";
import {Order} from "../../model/order";

/**
 * @author Dennis Thanner
 * @version 0.0.3 added tmp order - DT
 *          0.0.2 added grouping after loading - DT
 */
@Component({
  templateUrl: "order-select-modal.html",
  selector: "order-select-modal"
})
export class OrderSelectModalComponent {

  private tmpOrders: Array<Order> = [];

  constructor(private viewCtrl: ViewController, private menuCategoryService: MenuCategoryService, private menuService: MenuService,
              private modalCtrl: ModalController) {
    Promise.all([
      menuCategoryService.loadCategoryTree(),
      menuService.getAll()
    ]).then(() => {
      this.groupMenuToCategories();
    }, console.debug)

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
   * add menu to tmp order
   *
   * @param menu
   */
  addOrder(menu, sideDish, wish) {
    console.debug("add to tmp orders", menu, this.tmpOrders);
    this.tmpOrders.push(new Order(menu, sideDish, wish));
  }

  /**
   * open menu detail modal
   */
  openDetailModal(menuId: string) {
    let menuModal = this.modalCtrl.create(MenuDetailModalComponent, {id: menuId});
    menuModal.present();
  }

  /**
   * get tmp order count
   *
   * @param menu
   * @returns {number}
   */
  getTmpOrderCount(menu) {
    return this.tmpOrders.filter(el => {
      return el.item.id == menu.id
    }).length;
  }

  /**
   * close modal
   */
  dismiss() {
    this.viewCtrl.dismiss(this.tmpOrders);
  }

}
