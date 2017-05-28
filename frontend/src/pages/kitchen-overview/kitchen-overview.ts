import {Component} from "@angular/core";
import {AlertController, LoadingController, NavController, NavParams} from "ionic-angular";
import {AuthGuardService} from "../../service/auth-guard.service";
import {OrderService} from "../../service/order.service";
import {MenuCategoryService} from "../../service/menu-category.service";
import {OrderSocketService} from "../../service/order-socket-service";


@Component({
  templateUrl: "kitchen-overview.html",
})

export class KitchenOverviewPage {

  private loading;

  //if value is 1 the kitchen with meals will be shown, otherwise the bar with drinks is shown
  private forKitchen;
  private socketTopic;

  constructor(public navParams: NavParams,
              private navCtrl: NavController,
              private orderService: OrderService,
              private authGuard: AuthGuardService,
              public loadingCtrl: LoadingController,
              private menuCategoryService: MenuCategoryService,
              private alertCtrl: AlertController,
              private orderSocketService: OrderSocketService) {

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
    });

    this.orderSocketService.subscribe(this.socketTopic,
      data => {

        for (var key in data) {

          if (key == "desks") {
            //group by category and save to cache
            this.groupByCategory(data[key]);
            this.orderService.cache['open_orders_grouped_by_desks'] = data[key];
            console.debug("--->Category!!!!!!!", data[key]);
          }

          else if (key == "items") {
            //save to cache
            this.orderService.cache['open_orders_grouped_by_orderitem'] = data[key];
          }

        }
      });

  }

  /**
   * on page leave unsubscribe
   */
  ionWillLeaveView() {
    this.orderSocketService.disconnect();
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

    var catGroups = [];

    for (let cat of this.menuCategoryService.cache["tree"]) {
      let catIds = this.getChildCategoryIds(cat);
      catIds.push(cat.id);
      catGroups.push({category: cat.name, itemsPerCat: this.getOrdersByCat(catIds, ordersPerDesk)});
    }

    ordersPerDesk.length = 0;

    for (var cat of catGroups) {
      if (cat.itemsPerCat.length != 0) {
        ordersPerDesk.push({
          'category': cat.category,
          'itemsPerCat': cat.itemsPerCat
        });
      }
    }

  }

  /**
   * get orders by root category ids with child ids
   *
   * @param catIds
   */
  getOrdersByCat(catIds, ordersPerDesk) {
    return ordersPerDesk.filter(el => {
      return catIds.indexOf(el.item.menuItemCategory.id) != -1;
    });
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


  /**
   * creates a checkboxAlert to declare the status of all or specific orderItems of a category as finished
   * @param desknumber, orderPerDesk
   */
  presentConfirmCategory(desknumber, cat) {
    let alert = this.alertCtrl.create();
    alert.setTitle(cat.category + ' für Tisch ' + desknumber + ' fertigstellen?');

    for (var orderItems of cat.itemsPerCat) {
      var labelText;
      labelText = orderItems.orderIds.length + " x " + orderItems.item.shortName;

      orderItems.sideDishes.length != 0 ? labelText += " - " : "";
      for(var sideDish of orderItems.sideDishes){
        labelText += sideDish.shortName + " ";
      }
      orderItems.guestWish.length != 0 ? labelText += " Wunsch: " + orderItems.guestWish : "";


      alert.addInput({
        type: 'checkbox',
        label:  labelText,
        value: orderItems,
        checked: true
      });
    }

    alert.addButton('Abbruch');
    alert.addButton({
      text: 'Fertigstellen',
      handler: data => {
        var ids;
        ids = '';

        for (var orderItems of data) {
          for (var orderIds of orderItems.orderIds) {
            ids += orderIds + ",";
          }
        }
        this.sendingData(ids);
      }
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
