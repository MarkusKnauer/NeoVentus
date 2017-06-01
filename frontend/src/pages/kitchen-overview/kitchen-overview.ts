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

    this.initSocket();

  }

  /**
   * initializes the orderSocket
   */
  initSocket() {
    this.orderSocketService.subscribe(this.socketTopic,
      data => {

        for (let key in data) {

          if (key == "desks") {
            //group by category and save to cache
            this.groupByCategory(data[key]);
            this.orderService.cache['open_orders_grouped_by_desks'] = data[key];
            this.checkForNewItems(data[key]);
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
  ionViewWillLeave() {
    this.orderSocketService.unsubscribe();
  }

  loadOrderItemsGroupedByDeskAndCategory() {
    this.orderService.getAllOpenOrderItemsGroupedByDesk(this.forKitchen)
      .then(data => {
        this.groupByCategory(data);
        this.copyMap(data);

      })
  }

  /**
   * sorts orderItems by category
   * @param orderitems
   */

  groupByCategory(orderItems) {
    if (orderItems != null) {
      for (let key in orderItems) {
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

    let catGroups = [];

    for (let cat of this.menuCategoryService.cache["tree"]) {
      let catIds = this.getChildCategoryIds(cat);
      catIds.push(cat.id);
      catGroups.push({category: cat.name, itemsPerCat: this.getOrdersByCat(catIds, ordersPerDesk)});
    }

    ordersPerDesk.length = 0;

    for (let cat of catGroups) {
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

            let ids;
            ids = '';

            // go through the hierachy per desk
            for (let itemsPerCat of ordersPerDesk) {
              for (let orderItems of itemsPerCat.itemsPerCat) {
                for (let orderIds of orderItems.orderIds) {
                  ids += orderIds + ",";
                }

                this.deleteItemInOverview(orderItems);
              }
            }

            this.orderService.setOrderItemStateFinished(ids).toPromise();

            //delete desk
            this.orderService.cache['open_orders_grouped_by_desks'][deskNumber].length = 0;
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
  presentConfirmCategory(deskNumber, cat) {
    let alert = this.alertCtrl.create();
    alert.setTitle(cat.category + ' für Tisch ' + deskNumber + ' fertigstellen?');
    alert.setMessage
    for (let orderItems of cat.itemsPerCat) {
      var labelText;
      labelText = orderItems.orderIds.length + " x " + orderItems.item.shortName;

      orderItems.sideDishes.length != 0 ? labelText += " - " : "";
      for (let sideDish of orderItems.sideDishes) {
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

        let ids;
        ids = '';

        for (let orderItems of data) {
          for (let orderIds of orderItems.orderIds) {
            ids += orderIds + ",";
          }

          this.deleteItemInOverview(orderItems);
          this.deleteItemInCard(cat, orderItems, deskNumber);
        }

        this.orderService.setOrderItemStateFinished(ids).toPromise();

      }
    });
    alert.present();
  }

  /**
   * deletes or reduces an orderItems in the right overview
   *
   * @param orderItem
   */
  deleteItemInOverview(orderItem) {

    let rmOrder = this.orderService.cache['open_orders_grouped_by_orderitem'].find((el) => {
      return el.item.id == orderItem.item.id;
    });

    let rmOrderIndex = this.orderService.cache['open_orders_grouped_by_orderitem'].indexOf(rmOrder);

    if (rmOrder.count == orderItem.orderIds.length) {
      // delete the whole orderItem
      this.orderService.cache['open_orders_grouped_by_orderitem'].splice(rmOrderIndex, 1);
    } else {
      //reduce the count
      rmOrder.count -= orderItem.orderIds.length;
    }
  }

  /**
   * deletes an orderItems in a spezific card
   *
   * @param cat
   * @param orderItem
   * @param deskNumber
   */
  deleteItemInCard(cat, orderItem, deskNumber) {
    //delete item in cards
    let rmOrder = cat.itemsPerCat.find((el) => {
      return el.item.id == orderItem.item.id;
    });

    let rmOrderIndex = cat.itemsPerCat.indexOf(rmOrder);
    cat.itemsPerCat.splice(rmOrderIndex, 1);

    if (cat.itemsPerCat.length == 0) {
      let catIndex = this.orderService.cache['open_orders_grouped_by_desks'][deskNumber].indexOf(cat);
      this.orderService.cache['open_orders_grouped_by_desks'][deskNumber].splice(catIndex, 1);
    }
  }

  /**
   * copies the map of orderItems per desk in a new one
   * @param map
   */
  copyMap(map) {
    var newMap = {};

    for (let key in map) {
      newMap[key] = map[key];
    }

    this.orderService.cache['old_orderItems'] = newMap;
  }


  checkForNewItems(data) {
    let tmpOrder;
    let newIndex;

    let checkMap = this.orderService.cache['old_orderItems'];
    //checkMap[key][catIndex].itemsPerCat[orderIndex].orderIds.length

    for (let key in checkMap) {

      //desk do not have any open orders
      if (data[key].length == 0) continue;
      let existingCats = [];

      checkMap[key].forEach((category) => {

        // find the new position of the old category
        tmpOrder = data[key].find((el) => {
          return el.category == category.category;
        });
        var newCatIndex = data[key].indexOf(tmpOrder);
        existingCats.push(newCatIndex);

        //added new items in a category
        if (category.itemsPerCat.length < data[key][newCatIndex].itemsPerCat.length) {
          let indexTil = 9999999999;

          for (let orderItem of category.itemsPerCat) {

            // find new position of the old orderItems
            // itemsPerCat Array is like a stack - new orders are put to the top
            tmpOrder = data[key][newCatIndex].itemsPerCat.find((el) => {
              return el.item.id == orderItem.item.id;
            });
            newIndex = data[key][newCatIndex].itemsPerCat.indexOf(tmpOrder);
            newIndex < indexTil ? indexTil = newIndex : "";
          }

          //loop until the old orderItems
          for (let i = 0; i < indexTil; i++) {
            //ToDo do cool stuff
            this.markOrderAsNew(data[key][newCatIndex].itemsPerCat[i]);
            console.debug("Neue Bestellung pro Kategorie", data[key][newCatIndex].itemsPerCat[i].item.name, "Tisch ", key);
          }
        }


        // an existing item  has increased
        category.itemsPerCat.forEach((orderItem) => {

          // find new position of the old orderItems
          tmpOrder = data[key][newCatIndex].itemsPerCat.find((el) => {
            return el.item.id == orderItem.item.id;
          });
          newIndex = data[key][newCatIndex].itemsPerCat.indexOf(tmpOrder);

          if (newIndex != -1 && data[key][newCatIndex].itemsPerCat[newIndex].orderIds.length > orderItem.orderIds.length) {
            //ToDo do cool stuff
            this.markOrderAsNew(data[key][newCatIndex].itemsPerCat[newIndex]);
            console.debug("Bestellerhöhung für", data[key][newCatIndex].itemsPerCat[newIndex].item.name, "Tisch ", key);
          }
        });

      });

      //added a new category
      if (checkMap[key].length < data[key].length) {

        for (let i = 0; i < data[key].length; i++) {
          if (existingCats.indexOf(i) != -1) continue;
          else {
            for (let orderItem of data[key][i].itemsPerCat) {
              //todo do cool stuff
              this.markOrderAsNew(orderItem);
              console.debug("neue Kategorie ", data[key][i].category, "item ", orderItem.item.name, "für Tisch", key);
            }
          }
        }
      }
    }
    this.copyMap(data);
    console.debug("Data", data);
  }

  markOrderAsNew(item) {
    //todo add new attribute
    //item.push({
    //  'state' : 'new'
    //})
  }

}
