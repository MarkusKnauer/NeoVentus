import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {CachingService} from "./caching.service";
import {OrderDto} from "../model/order-dto";

/**
 * handling requests to the backend belonging the orders
 *
 * @author Julian Beck, Dennis Thanner
 * @version 0.0.6 added insert batch order support - DT
 *          0.0.5 added caching support for kitchen/bar - DS
 *          0.0.4 changed url for getAllOpenOrderItems - DS
 *          0.0.3 changed urls
 */
@Injectable()
export class OrderService extends CachingService {

  private static BASE_URL = "/api/order";

  constructor(private http: Http) {
    super();
    this.http = http;
  }

  /**
   * Gets all orders by desk number
   *
   * @returns {Observable<Response>}
   */
  public getOrdersByDesk(desknumber: number) {

    return new Promise<any>(resolve => {
      this.http.get(OrderService.BASE_URL + "/desk/open/" + desknumber.toString())
        .map(res => res.json())
        .subscribe(order => {
          this.saveToCache("orders_desk" + desknumber.toString(), order);
          resolve(order);
        });
    });
  }

  /**
   * Gets all orders grouped by desk
   *
   * @returns {Observable<Response>}
   */
  public getAllOpenOrderItems() {

    return new Promise<any>(resolve => {
      this.http.get(OrderService.BASE_URL + "/all/open/meals")
        .map(res => res.json())
        .subscribe(data => {
          this.saveToCache("open_orders_grouped_by_desks", data);
          resolve(data);
        });
    });

  }

  /**
   * Gets all orders by orderItem
   *
   * @returns {Observable<Response>}
   */
  public getAllOpenOrderItemsGroupedByOrderItem() {

    return new Promise<any>(resolve => {
      this.http.get(OrderService.BASE_URL + "/all/open/meals")
        .map(res => res.json())
        .subscribe(data => {
          this.saveToCache("open_orders_grouped_by_orderitem", data);
          resolve(data);
        });
    });

  }


  /**
   * send order to server
   *
   * @returns {Observable<Response>}
   * @param orders
   */
  public insertOrders(orders: Array<OrderDto>) {
    return this.http.post(OrderService.BASE_URL, orders);
  }

}
