import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {CachingService} from "./caching.service";
import {OrderDto} from "../model/order-dto";

/**
 * handling requests to the backend belonging the orders
 *
 * @author Julian Beck, Dennis Thanner, Tim Heidelbach
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
  public getOrdersByDeskNumber(deskNumber: number) {

    if (this.cache["orders_desk" + deskNumber] != null) {
      return new Promise<any>(resolve => {
        resolve(this.cache["orders_desk" + deskNumber]);
      })
    } else {
      return new Promise<any>(resolve => {
        this.http.get(OrderService.BASE_URL + "/desk/open/" + deskNumber)
          .map(res => res.json())
          .subscribe(order => {
            this.saveToCache("orders_desk" + deskNumber, order);
            resolve(order);
          });
      });
    }
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
