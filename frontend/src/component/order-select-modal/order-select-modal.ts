import {Component} from "@angular/core";
import {Events, ModalController, NavParams, ViewController} from "ionic-angular";
import {MenuCategoryService} from "../../service/menu-category.service";
import {MenuService} from "../../service/menu.service";
import {MenuDetailModalComponent} from "../menu-detail-modal/menu-detail-modal";
import {Order} from "../../model/order";
import {LocalStorageService} from "../../service/local-storage.service";

/**
 * @author Dennis Thanner
 * @version 0.0.5 finished detail modal and favs support - DT
 * 0.0.4 added load favorites - DT
 *          0.0.3 added tmp order - DT
 *          0.0.2 added grouping after loading - DT
 */
@Component({
  templateUrl: "order-select-modal.html",
  selector: "order-select-modal"
})
export class OrderSelectModalComponent {

  private tmpOrders: Array<Order> = [];

  constructor(private viewCtrl: ViewController, private menuCategoryService: MenuCategoryService, private menuService: MenuService,
              private modalCtrl: ModalController, private navParams: NavParams, private localStorageService: LocalStorageService,
              private events: Events) {
    this.tmpOrders = navParams.data.tmpOrders;
    Promise.all([
      menuCategoryService.loadCategoryTree(),
      menuService.getAll()
    ]).then(() => {
      this.groupMenuToCategories();
      this.localStorageService.loadMenuFavoriteIds();
    }, console.debug);

  }

  /**
   * get menu object by id from cache
   * @param menuId
   */
  getMenuObjectToId(menuId: string) {
    return this.menuService.cache["all"].find(elem => {
      return elem.id == menuId;
    });
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
   * add menu to tmp order or open detail modal
   *
   * @param menu
   * @param sideDishs
   * @param wish
   */
  addOrder(menu, sideDishes, wish) {
    if (!menu.sideDishGroup) {
      this.tmpOrders.push(new Order(menu, sideDishes, wish));
    } else {
      // if menu has side dishes open modal to select them
      this.openDetailModal(menu.id);
    }
  }

  /**
   * open menu detail modal
   */
  openDetailModal(menuId: string) {
    let menuModal = this.modalCtrl.create(MenuDetailModalComponent, {id: menuId});
    menuModal.present();
    menuModal.onDidDismiss((data) => {
      if (data) {
        this.tmpOrders.push(data);
      }
    })
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
    this.viewCtrl.dismiss();
  }

}
