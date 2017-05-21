import {Component} from "@angular/core";
import {NavController, LoadingController, AlertController, NavParams} from "ionic-angular";
import {AuthGuardService} from "../../service/auth-guard.service";
import {OrderService} from "../../service/order.service";
import {MenuCategoryService} from "../../service/menu-category.service";
import {OrderSocketService} from "../../service/order-socket-service";



@Component({
  templateUrl: "kitchen-overview.html",
})

export class KitchenOverviewPage {

  private loading;

  //if value is 1 the kitchen with meals will be shown, otherwhise the bar with drinks is shown
  private forKitchen;
  private socketTopic;

  constructor(public navParams: NavParams,
              private navCtrl: NavController,
              private orderService: OrderService,
              private authGuard: AuthGuardService,
              public loadingCtrl: LoadingController,
              private menuCategoryService: MenuCategoryService,
              private alertCtrl: AlertController,
              private orderSocketService: OrderSocketService
  ) {

    this.forKitchen = navParams.get("forKitchen");

    if (this.forKitchen == 1) {
      this.socketTopic = "/topic/order/kitchen";
    } else {
      this.socketTopic = "/topic/order/bar";
    }


    this.presentLoadingDefault("Bestellungen werden geladen");

    Promise.all([
      this.menuCategoryService.loadCategoryTree(),
      this.orderService.getAllOpenOrderItemsGroupedByOrderItem(this.forKitchen),
      this.loadOrderItemsGroupedByDeskAndCategory()
    ]).then(() => {
      this.loading.dismissAll();
    })


    this.orderSocketService.subscribe(this.socketTopic,
      data => {

        for (var key in data) {

          if (key == "desks") {
            //group by category and save to cache
            this.groupByCategory(data[key]);
            this.orderService.cache['open_orders_grouped_by_desks'] = data[key];
          }

          else if (key == "items") {
            //save to cache
            this.orderService.cache['open_orders_grouped_by_orderitem'] = data[key];
          }

        }
      });

  }

  loadOrderItemsGroupedByDeskAndCategory() {
    this.orderService.getAllOpenOrderItemsGroupedByDesk(this.forKitchen)
      .then(data => {
        this.groupByCategory(data);
      })
  }

  /**
   * sorts orderItems by category
   * @param orderitems
   */

  groupByCategory(orderItems) {
    if (orderItems != null) {
      for (var key in orderItems) {
        if (orderItems[key].length != 0) {
          this.groupByCategoryPerDesk(orderItems[key]);
        }
      }
    }
  }

  /**
   * sorts orderItems per desk by category
   * @param ordersPerDesk
   */

  groupByCategoryPerDesk(ordersPerDesk) {
    //new object with keys as item.name and
    //orderItem array as value
    var newArray = {};


    //iterate through each element of array
    ordersPerDesk.forEach((val) => {

      var key = this.getCategoryRootParent(val.item.menuItemCategory.id);
      var curr = newArray[key];

      //if array key doesnt exist, init with empty array
      if (!curr) {
        newArray[key] = [];
      }

      //append orderItem to this key
      newArray[key].push(val);
    });

    //remove elements from previous array
    ordersPerDesk.length = 0;

    //replace elements with new objects made of
    //key value pairs from our created object
    for (var key in newArray) {
      ordersPerDesk.push({
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

  /**
   * fancy loading
   * @param message
   */
  presentLoadingDefault(info) {
    this.loading = this.loadingCtrl.create({
      content: info
    });

    this.loading.present();
  }

  /**
   * sets the status of all orderItems per desk as finished
   * @param desknumber, orderPerDesk
   */
  presentConfirmAllOfDesk(deskNumber, ordersPerDesk) {
    let alert = this.alertCtrl.create({
      title: 'Alle Gerichte des Tisches ' + deskNumber + ' fertigstellen?',
      buttons: [
        {
          text: 'Abbruch',
          role: 'cancel',
        },
        {
          text: 'Fertigstellen',
          handler: () => {
            var ids;
            ids = '';
            // go through the hierachy per desk
            for (var itemsPerCat of ordersPerDesk) {
              for (var orderItems of itemsPerCat.itemsPerCat) {
                for (var orderIds of orderItems.orderIds) {
                  ids += orderIds + ",";
                }
              }
            }
            this.sendingData(ids);
          }
        }
      ]
    });
    alert.present();
  }


  presentConfirmCategory(desknumber, cat) {
    let alert = this.alertCtrl.create({
      title: cat.category + ' für Tisch ' + desknumber + ' fertigstellen?',
      buttons: [
        {
          text: 'Abbruch',
          role: 'cancel',
        },
        {
          text: 'Fertigstellen',
          handler: () => {
            var ids;
            ids = '';


            // go through the hierachy
            for (var orderItems of cat.itemsPerCat) {
              for (var orderIds of orderItems.orderIds) {
                ids += orderIds + ",";
              }
            }
            this.sendingData(ids);
          }
        }
      ]
    });
    alert.present();
  }

  /**
   * sending the data and reload
   * @param ids
   */
  sendingData(ids) {
    this.presentLoadingDefault("Bitte warten ...");

    this.orderService.setOrderItemStateFinished(ids).toPromise().then(() => {
      // optional - loading all orders new at once

      //this.orderService.getAllOpenOrderItemsGroupedByOrderItem(this.forKitchen),
      //  this.loadOrderItemsGroupedByDeskAndCategory();

      this.loading.dismissAll();
    });
  }

}


