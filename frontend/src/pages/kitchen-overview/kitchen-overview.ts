import {Component} from "@angular/core";
import {
  ActionSheetController,
  AlertController,
  LoadingController,
  NavController,
  NavParams,
  Platform
} from "ionic-angular";
import {OrderService} from "../../service/order.service";
import {MenuCategoryService} from "../../service/menu-category.service";
import {OrderSocketService} from "../../service/order-socket-service";
import {TextToSpeech} from "@ionic-native/text-to-speech";
import {LocalStorageService} from "../../service/local-storage.service";
import {AuthGuardService} from "../../service/auth-guard.service";
import {LoginPage} from "../login/login";
import {Role} from "../../app/roles";


@Component({
  templateUrl: "kitchen-overview.html",
  selector: "kitchen-overview"
})

export class KitchenOverviewPage {

  private loading;

  //if value is 1 the kitchen with meals will be shown, otherwise the bar with drinks is shown
  private forKitchen;
  private socketTopic;
  private newOrders: boolean;
  //30 seconds interval
  private timeInterval = 30000;

  constructor(public navParams: NavParams,
              private orderService: OrderService,
              public loadingCtrl: LoadingController,
              private menuCategoryService: MenuCategoryService,
              private alertCtrl: AlertController,
              private orderSocketService: OrderSocketService,
              private tts: TextToSpeech,
              private platform: Platform,
              private actionSheetCtrl: ActionSheetController,
              private localStorageService: LocalStorageService,
              private authGuard: AuthGuardService,
              private navCtrl: NavController) {

    this.localStorageService.loadStornoReasons();

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
   * view life cycle method
   *
   * RBMA
   */
  ionViewWillEnter() {
    this.authGuard.hasAnyRolePromise([this.forKitchen == 1 ? Role.CHEF : Role.BAR, Role.CEO]).then(() => {
    }, () => {
      console.debug("RBMA - Access denied!");
      this.navCtrl.setRoot(LoginPage);
    });
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
                this.deleteItemInOverview(orderItems, orderItems.orderIds.length);
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
        label: labelText,
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

          this.deleteItemInOverview(orderItems, orderItems.orderIds.length);
          this.deleteItemInCard(cat, orderItems, deskNumber, orderItems.orderIds.length);
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
   * @param reduction
   */
  deleteItemInOverview(orderItem, reduction) {

    let rmOrder = this.orderService.cache['open_orders_grouped_by_orderitem'].find((el) => {
      return el.item.id == orderItem.item.id;
    });

    let rmOrderIndex = this.orderService.cache['open_orders_grouped_by_orderitem'].indexOf(rmOrder);

    if (rmOrder.count == reduction) {
      // delete the whole orderItem
      this.orderService.cache['open_orders_grouped_by_orderitem'].splice(rmOrderIndex, 1);
    } else {
      //reduce the count
      rmOrder.count -= reduction;
    }
  }

  /**
   * deletes an orderItems in a spezific card
   * @param cat
   * @param orderItem
   * @param deskNumber
   * @param reduction
   */
  deleteItemInCard(cat, orderItem, deskNumber, reduction) {
    //delete item in cards
    let rmOrder = cat.itemsPerCat.find((el) => {
      return el.item.id == orderItem.item.id;
    });

    let rmOrderIndex = cat.itemsPerCat.indexOf(rmOrder);

    if (reduction == rmOrder.orderIds.length) {
      cat.itemsPerCat.splice(rmOrderIndex, 1);
    } else {
      rmOrder.orderIds.splice(0, reduction);
    }


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
    let hasNewOrders = false;

    //go through desks
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


          if (newCatIndex != -1) {
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

              if (newIndex != -1) {
                //CASE 1 - an existing item  has increased
                let diffCount = data[key][newCatIndex].itemsPerCat[newIndex].orderIds.length - orderItem.orderIds.length;
                if (newIndex != -1 && diffCount > 0) {
                  let oldCount = orderItem.newItemCount;
                  oldCount != null ? diffCount += oldCount : "";
                  this.markOrderAsNew(data[key][newCatIndex].itemsPerCat[newIndex], 0, diffCount);
                  hasNewOrders = true;
                }

                //CASE 2 - nothing changed, check if item is marked as new
                if (newIndex != -1 &&
                  data[key][newCatIndex].itemsPerCat[newIndex].orderIds.length == orderItem.orderIds.length &&
                  orderItem.isNew != null &&
                  orderItem.isNew == true) {
                  this.markOrderAsNew(data[key][newCatIndex].itemsPerCat[newIndex], orderItem.stateTimeUntil, orderItem.newItemCount);
                  hasNewOrders = true;
                }
              }
            });
          }
        });

        //CASE 3 - added a new category to a existing desk
        if (checkMap[key].length < data[key].length) {
          for (let i = 0; i < data[key].length; i++) {
            if (existingCats.indexOf(i) != -1) continue;
            else {
              for (let orderItem of data[key][i].itemsPerCat) {
                this.markOrderAsNew(orderItem, 0, 0);
                hasNewOrders = true;
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

    if (this.platform.is("cordova") && this.newOrders) {
      this.tts.speak({
        text: "Neue Bestellungen eingetroffen",
        locale: "de-DE"
      });
      this.newOrders = false;
    }
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
      this.newOrders = true;
    } else {
      item.stateTimeUntil = oldTimestamp;
    }

    let sleepTime = item.stateTimeUntil - Date.now();
    setTimeout(function () {
      item.isNew = false;
      delete item.newItemCount;
    }, sleepTime);
  }

  /**
   * open storno alert to chose storno all or one
   * @param deskNumber
   * @param cat
   * @param item
   */
  openStornoAlert(deskNumber, cat, item) {
    let alert = this.actionSheetCtrl.create();
    alert.setTitle(item.item.name + ' des Tisches ' + deskNumber + ' stornieren?');

    alert.addButton({
      text: 'Storno einzeln',
      handler: () => {
        this.openStornoAction(deskNumber, cat, item, false);
      }
    });

    if (item.orderIds.length > 1) {
      alert.addButton({
        text: 'Storno alle',
        handler: () => {
          this.openStornoAction(deskNumber, cat, item, true);
        }
      });
    }

    alert.addButton('Abbruch');

    alert.present();
  }

  /**
   * open storno popup to chose which order to abort
   * @param deskNumber
   * @param cat
   * @param item
   * @param all
   */
  openStornoAction(deskNumber, cat, item, all: boolean,) {
    let orderIds = [];
    if (all) {
      orderIds = item.orderIds;
    } else {
      orderIds.push(item.orderIds[0]);
    }

    // get clickable buttons
    let buttons = [];
    for (let reason of this.localStorageService.cache[LocalStorageService.STORNO_REASONS_KEY]) {
      buttons.push({
        text: reason,
        handler: () => {
          actionSheet.dismiss(reason);
          return false;
        }
      });
    }
    buttons.push({text: "Abbrechen", role: "cancel"});

    let actionSheet = this.actionSheetCtrl.create({
      title: "Stornierungsgrund",
      buttons: buttons
    });
    actionSheet.present();

    // after close reload data
    actionSheet.onDidDismiss((reason) => {
      // only execute if reason is set
      if (reason) {
        this.orderService.cancelOrders(orderIds, reason).toPromise().then(() => {
          //fake performance
          this.deleteItemInCard(cat, item, deskNumber, orderIds.length);
          this.deleteItemInOverview(item, orderIds.length);
        });
      }
    })
  }

}
