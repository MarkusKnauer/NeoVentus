import {Component} from "@angular/core";
import {NavController, LoadingController} from "ionic-angular";
import {AuthGuardService} from "../../service/auth-guard.service";
import {OrderService} from "../../service/order.service";
import {MenuCategoryService} from "../../service/menu-category.service";
import {OrderSocketService} from "../../service/order-socket-service";



/**
 * @author Dominik Streif
 * @version 0.0.3 group by category - DS
 *          0.0.2 added cache support - DS
 *          0.0.1
 *
 */

@Component({
  templateUrl: "kitchen-overview.html",
})

export class KitchenOverviewPage {

  private loading;

  // ToDo show category and order items
  // ToDo filter - only meals/drinks

  constructor(private navCtrl: NavController,
              private orderService: OrderService,
              private authGuard: AuthGuardService,
              public loadingCtrl: LoadingController,
              private menuCategoryService: MenuCategoryService
              //private orderSocketService: OrderSocketService
  ) {

    this.presentLoadingDefault();

    Promise.all([
      this.menuCategoryService.loadCategoryTree(),
      this.loadOrderItemsGroupedByDesk(),
      this.loadOrderItemsGroupedByOrderItem()
    ]).then(() => {
      this.loading.dismissAll();
    })

    // ToDo Socket Callback function
    //this.orderSocketService.subscribe(cb?);
  }


  loadOrderItemsGroupedByOrderItem() {
    this.orderService.getAllOpenOrderItemsGroupedByOrderItem()
      .then(
        data => {
          this.groupByOrderItem(data);
        })
  }

  loadOrderItemsGroupedByDesk() {
    this.orderService.getAllOpenOrderItems()
      .then(
        data2 => {
          //this.orderItems = data;
          this.groupByDesk(data2);
        })
  }

  groupByDesk(orderItems) {
    //new object with keys as desk.number and
    //orderItem array as value
    var newArray = {};

    //iterate through each element of array
    orderItems.forEach(function (val) {
      var key = val.desk.number;
      var curr = newArray[key];

      //if array key doesnt exist, init with empty array
      if (!curr) {
        newArray[key] = [];
      }

      //append orderItem to this key
      newArray[key].push(val);
    });

    //remove elements from previous array
    orderItems.length = 0;

    //replace elements with new objects made of
    //key value pairs from our created object
    for (var key in newArray) {
      this.groupByOrderItem(newArray[key]);
      this.groupByCategory(newArray[key]);
      orderItems.push({
        'desk': key,
        'categories': newArray[key]
      });
    }
  }

  groupByOrderItem(orderItemGrouped) {
    //new object with keys as item.name and
    //orderItem array as value
    var newArray = {};

    //iterate through each element of array
    orderItemGrouped.forEach(function (val) {
      var key = (val.item.name).concat(val.guestWish);

      var curr = newArray[key];

      //if array key doesnt exist, init with empty array
      if (!curr) {
        newArray[key] = [];
      }

      //append orderItem to this key
      newArray[key].push(val);
    });

    //remove elements from previous array
    orderItemGrouped.length = 0;

    //replace elements with new objects made of
    //key value pairs from our created object
    for (var key in newArray) {
      orderItemGrouped.push({
        'quantity': newArray[key].length,
        'orderItem': newArray[key]
      });
    }


  }

  groupByCategory(orderItems) {
    //new object with keys as item.name and
    //orderItem array as value
    var newArray = {};


    //iterate through each element of array
    orderItems.forEach((val) => {

      var key = this.getCategoryRootParent(val.orderItem['0'].item.menuItemCategory.id);
      var curr = newArray[key];

      //if array key doesnt exist, init with empty array
      if (!curr) {
        newArray[key] = [];
      }

      //append orderItem to this key
      newArray[key].push(val);
    });

    //remove elements from previous array
    orderItems.length = 0;

    //replace elements with new objects made of
    //key value pairs from our created object
    for (var key in newArray) {
      orderItems.push({
        'category': key,
        'itemsPerCat': newArray[key]
      });
    }
  }

  /**
   * get the root parent of a spezific subcategory
   * @param id
   */
  getCategoryRootParent(id) {
    for (let cat of this.menuCategoryService.cache["tree"]) {
      let catIds = this.getChildCategoryIds(cat);
      catIds.push(cat.id);
      if (catIds.indexOf(id) != -1) {
        return cat.name
      }
    }
  }


  /**
   * traverse menu category tree to get a id array
   * @param cat
   */
  getChildCategoryIds(cat) {
    return cat.subcategory.map((child) => {
      return [child.id].join(this.getChildCategoryIds(child));
    });
  }



  // Fancy Loading circle
  presentLoadingDefault() {
    this.loading = this.loadingCtrl.create({
      content: 'Bestellungen werden geladen.'
    });

    this.loading.present();
  }
}
