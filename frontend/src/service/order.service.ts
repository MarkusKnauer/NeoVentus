import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {CachingService} from "./caching.service";

/**
 * handling requests to the backend belonging the orders
 *
 * @author Julian Beck, Dennis Thanner
 * @version   0.0.5 added caching support for kitchen/bar - DS
 *            0.0.4 changed url for getAllOpenOrderItems - DS
 *            0.0.3 changed urls
 */
@Injectable()
export class OrderService extends CachingService {

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
      this.http.get("/api/order/desk/open/" + desknumber.toString())
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
   * @param
   * @returns {Observable<Response>}
   */
  public getAllOpenOrderItems() {

    return new Promise<any>(resolve => {
      this.http.get("/api/order/all/open/meals")
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
   * @param
   * @returns {Observable<Response>}
   */
  public getAllOpenOrderItemsGroupedByOrderItem() {

    return new Promise<any>(resolve => {
      this.http.get("/api/order/all/open/meals")
        .map(res => res.json())
        .subscribe(data => {
          this.saveToCache("open_orders_grouped_by_orderitem", data);
          resolve(data);
        });
    });

  }

}
