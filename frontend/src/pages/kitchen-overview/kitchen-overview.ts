import {Component} from "@angular/core";
import {NavController} from "ionic-angular";
import {AuthGuardService} from "../../service/auth-guard.service";
import {OrderService} from "../../service/order.service";

/**
 * @author Dominik Streif
 * @version 0.0.1
 *
 */

@Component({
  templateUrl: "kitchen-overview.html",
})

export class KitchenOverviewPage {

  public orderItems: any;
  public orderItemsGrouped: any;

  //ToDo nach Kategorie filter
  //ToDo Socketverbindung

  constructor(private navCtrl: NavController, private orderService: OrderService, private authGuard: AuthGuardService) {
    this.loadOrderItemsGroupedByOrderItem();
    this.loadOrderItemsGroupedByDesk();
  }


  loadOrderItemsGroupedByOrderItem() {
    this.orderService.getAllOrderItemsByState("NEW")
      .then(
        data => {
          this.groupByOrderItem(data);
          this.orderItemsGrouped = data;
        })
  }

  loadOrderItemsGroupedByDesk() {
    this.orderService.getAllOrderItemsByState("NEW")
      .then(
        data => {
          this.groupByDesk(data);
          this.orderItems = data;
        })
  }


  groupByDesk(orderItems) {
    //new object with keys as desk.number and
    //orderItem array as value
    var newArray = {};

    //iterate through each element of array
    orderItems.forEach(function (val) {
      var curr = newArray[val.desk.number];

      //if array key doesnt exist, init with empty array
      if (!curr) {
        newArray[val.desk.number] = [];
      }

      //append orderItem to this key
      newArray[val.desk.number].push(val);
    });

    //remove elements from previous array
    orderItems.length = 0;

    //replace elements with new objects made of
    //key value pairs from our created object
    for (var key in newArray) {
      this.groupByOrderItem(newArray[key]);
      orderItems.push({
        'desk': key,
        'orderItem': newArray[key]
      });
    }
  }

  groupByOrderItem(orderItemGrouped) {
    //new object with keys as item.name and
    //orderItem array as value
    var newArray = {};

    //iterate through each element of array
    orderItemGrouped.forEach(function (val) {

      var curr = newArray[(val.item.name).concat(val.guestWish)];

      //if array key doesnt exist, init with empty array
      if (!curr) {
        newArray[(val.item.name).concat(val.guestWish)] = [];
      }

      //append orderItem to this key
      newArray[(val.item.name).concat(val.guestWish)].push(val);
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

}