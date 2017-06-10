import {Component} from "@angular/core";
import {AlertController, LoadingController, NavParams} from "ionic-angular";
import {OrderService} from "../../service/order.service";
import {MenuCategoryService} from "../../service/menu-category.service";
import {OrderSocketService} from "../../service/order-socket-service";


@Component({
  templateUrl: "kitchen-overview.html",
  selector: "kitchen-overview"
})

export class KitchenOverviewPage {

  private loading;

  //if value is 1 the kitchen with meals will be shown, otherwise the bar with drinks is shown
  private forKitchen;
  private socketTopic;
  //30 seconds interval
  private timeInterval = 30000;

  constructor(public navParams: NavParams,
              private orderService: OrderService,
              public loadingCtrl: LoadingController,
              private menuCategoryService: MenuCategoryService,
              private alertCtrl: AlertController,
              private orderSocketService: OrderSocketService) {

    this.forKitchen = navParams.get("forKitchen");

    if (this.forKitchen == 1) {
      this.socketTopic = "/topic/order/kitchen";
    } else if (this.forKitchen == 0) {
      this.socketTopic = "/topic/order/bar";
    }

    this.presentLoadingDefault("Bestellungen werden geladen");

    Promise.all([
      this.menuCategoryService.loadCategoryTree(),
      this.orderService.getAllOpenOrderItemsGroupedByOrderItem(this.forKitchen),
      this.loadOrderItemsGroupedByDeskAndCategory()
    ]).then(() => {
      this.loading.dismiss();
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

  /**
   * load orderItems an group them by desk and category
   */
  loadOrderItemsGroupedByDeskAndCategory() {
    this.orderService.getAllOpenOrderItemsGroupedByDesk(this.forKitchen)
      .then(data => {
        this.groupByCategory(data);

        //check if cache is empty, to keep the marked orders in case of a page leave
        if (this.orderService.cache['old_orderItems'] == null || this.orderService.cache['forKitchen'] != this.forKitchen) {
          this.copyMap(data);
        } else {
          this.checkForNewItems(data);
        }
        this.orderService.cache['forKitchen'] = this.forKitchen;
      })
  }

  /**
   * sorts orderItems by category
   * @param orderItems
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
   * @param catIds
   * @param ordersPerDesk
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
   * @param info
   */
  presentLoadingDefault(info) {
    this.loading = this.loadingCtrl.create({
      content: info
    });
    this.loading.present();
  }

  /**
   * sets the status of all orderItems per desk as finished
   * @param deskNumber
   * @param ordersPerDesk
   */
  presentConfirmAllOfDesk(deskNumber, ordersPerDesk) {
    let alert = this.alertCtrl.create({
      title: 'Alle Gerichte des Tisches ' + deskNumber + ' fertigstellen?',

      buttons: [{
          text: 'Abbruch',
          role: 'cancel',
        },
        {
          text: 'Fertigstellen',
          handler: () => {

            let ids = '';

            // go through the hierachy per desk and get the orderIDs
            for (let itemsPerCat of ordersPerDesk) {
              for (let orderItems of itemsPerCat.itemsPerCat) {
                for (let orderIds of orderItems.orderIds) {
                  ids += orderIds + ",";
                }
                this.deleteItemInOverview(orderItems);
              }
            }

            this.orderService.finishOrders(ids).toPromise();

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
   * @param deskNumber
   * @param cat
   */
  presentConfirmCategory(deskNumber, cat) {
    let alert = this.alertCtrl.create();
    alert.setTitle(cat.category + ' für Tisch ' + deskNumber + ' fertigstellen?');
    alert.setMessage
    for (let orderItems of cat.itemsPerCat) {
      let labelText = orderItems.orderIds.length + " x " + orderItems.item.shortName;

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
        // check if something is selected, otherwhise do nothing
        ids != '' ? this.orderService.finishOrders(ids).toPromise() : '';
      }
    });
    alert.present();
  }

  /**
   * deletes or reduces an orderItems in the right overview
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
    let newMap = {};

    for (let key in map) {
      newMap[key] = map[key];
    }

    this.orderService.cache['old_orderItems'] = newMap;
  }

  /**
   * checks the data if there are new orders
   * @param data
   */
  checkForNewItems(data) {
    let tmpOrder;
    let newIndex;
    let checkMap = this.orderService.cache['old_orderItems'];

    for (let key in data) {

      //check if a desk already exists
      let keyExists = key in checkMap;
      let existingCats = [];

      if (keyExists) {

        checkMap[key].forEach((category) => {
          // find the new position of the old category
          tmpOrder = data[key].find((el) => {
            return el.category == category.category;
          });
          let newCatIndex = data[key].indexOf(tmpOrder);
          existingCats.push(newCatIndex);

          //CASE 0 - added new items in a category
          if (category.itemsPerCat.length < data[key][newCatIndex].itemsPerCat.length) {

            let indexTil = 9999999999;
            for (let orderItem of category.itemsPerCat) {
              // itemsPerCat Array behaves like a stack - new orders are put to the top
              // find new position of the old orderItems
              tmpOrder = data[key][newCatIndex].itemsPerCat.find((el) => {
                return el.item.id == orderItem.item.id;
              });
              newIndex = data[key][newCatIndex].itemsPerCat.indexOf(tmpOrder);
              newIndex < indexTil ? indexTil = newIndex : "";
            }

            //loop until the old orderItems
            for (let i = 0; i < indexTil; i++) {
              this.markOrderAsNew(data[key][newCatIndex].itemsPerCat[i], 0, 0);
            }
          }

          category.itemsPerCat.forEach((orderItem) => {

            // find new position of the old orderItems
            tmpOrder = data[key][newCatIndex].itemsPerCat.find((el) => {
              return el.item.id == orderItem.item.id;
            });
            newIndex = data[key][newCatIndex].itemsPerCat.indexOf(tmpOrder);

            //CASE 1 - an existing item  has increased
            let diffCount = data[key][newCatIndex].itemsPerCat[newIndex].orderIds.length - orderItem.orderIds.length;
            if (newIndex != -1 && diffCount > 0) {
              let oldCount = orderItem.newItemCount;
              oldCount != null ? diffCount += oldCount : "";
              this.markOrderAsNew(data[key][newCatIndex].itemsPerCat[newIndex], 0, diffCount);
            }

            //CASE 2 - nothing changed, check if item is marked as new
            if (newIndex != -1 &&
              data[key][newCatIndex].itemsPerCat[newIndex].orderIds.length == orderItem.orderIds.length &&
              orderItem.isNew != null &&
              orderItem.isNew == true) {
              this.markOrderAsNew(data[key][newCatIndex].itemsPerCat[newIndex], orderItem.stateTimeUntil, orderItem.newItemCount);
            }
          });

        });

        //CASE 3 - added a new category to a existing desk
        if (checkMap[key].length < data[key].length) {
          for (let i = 0; i < data[key].length; i++) {
            if (existingCats.indexOf(i) != -1) continue;
            else {
              for (let orderItem of data[key][i].itemsPerCat) {
                this.markOrderAsNew(orderItem, 0, 0);
              }
            }
          }
        }
      }

      //CASE 4 - added a new desk
      else {
        data[key].forEach((category) => {
          category.itemsPerCat.forEach((orderItem) => {
            this.markOrderAsNew(orderItem, 0, 0);
          })
        })
      }

    }
    this.copyMap(data);
  }

  /**
   * marks a Order as new an sets the state to default after a spezific time
   * @param item
   * @param oldTimestamp
   * @param diffCount
   */
  markOrderAsNew(item, oldTimestamp, diffCount) {

    item.isNew = true;

    if (diffCount != 0) {
      item.newItemCount = diffCount;
    }

    if (oldTimestamp == 0) {
      item.stateTimeUntil = Date.now() + this.timeInterval;
    } else {
      item.stateTimeUntil = oldTimestamp;
    }

    let sleepTime = item.stateTimeUntil - Date.now();
    setTimeout(function () {
      item.isNew = false;
      delete item.newItemCount;
    }, sleepTime);
  }

}
