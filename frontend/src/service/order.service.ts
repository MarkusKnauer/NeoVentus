import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {CachingService} from "./caching.service";
import {OrderDto} from "../model/order-dto";

/**
 * handling requests to the backend belonging the orders
 *
 * @author Julian Beck, Dennis Thanner
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
  public getAllOpenOrderItemsGroupedByDesk(forKitchen) {

    return new Promise<any>(resolve => {
      this.http.get(OrderService.BASE_URL + "/unfinished/grouped/by-desk/" + forKitchen.toString())
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
  public getAllOpenOrderItemsGroupedByOrderItem(forKitchen) {

    return new Promise<any>(resolve => {
      this.http.get(OrderService.BASE_URL + "/unfinished/grouped/by-item/" + forKitchen.toString())
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

  /**
   * get details for a specific order
   *
   * @param id
   * @returns {Promise<T>}
   */
  public getOrderInfo(id: string) {
    return this.http.get(OrderService.BASE_URL + "/" + id).toPromise();
  }

  /**
   * cancel orders by ids
   *
   * @param orderIds
   * @returns {Observable<Response>}
   */
  public cancleOrders(orderIds: Array<string>) {
    return this.http.put(OrderService.BASE_URL + "/cancel/" + orderIds.join(","), {});
  }


  public setOrderItemStateFinished(ids) {
    console.debug("HTTP-PUT finished Orders: ", ids);
    return this.http.put(OrderService.BASE_URL + "/finish", ids);

  }



}
