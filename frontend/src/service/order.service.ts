import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {CachingService} from "./caching.service";
import {OrderDto} from "../model/order-dto";
import {HttpService, ServiceUtils} from "./service-utils";
import {Events, Platform} from "ionic-angular";

/**
 * handling requests to the backend belonging the orders
 *
 * @author Julian Beck, Dennis Thanner, Tim Heidelbach
 */
@Injectable()
export class OrderService extends CachingService implements HttpService {

  BASE_URL_PREFIX = "/api/order";
  BASE_URL = this.BASE_URL_PREFIX;

  constructor(private http: Http, platform: Platform, events: Events) {
    super();
    ServiceUtils.initConnectionUrl(this, platform, events);
  }

  /**
   * Gets all orders by desk number
   *
   * @returns {Promise<any>}
   * @param deskNumber
   * @param force if method is forced to reload
   */
  public getOrdersByDeskNumber(deskNumber: number, force?: boolean) {

    if (this.cache["orders_desk" + deskNumber] != null && !force) {
      return new Promise<any>(resolve => {
        resolve(this.cache["orders_desk" + deskNumber]);
      })
    } else {
      return new Promise<any>(resolve => {
        this.http.get(this.BASE_URL + "/desk/open/" + deskNumber)
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
  public getAllOpenOrderItemsGroupedByDesk(forKitchen) {

    return new Promise<any>(resolve => {
      this.http.get(this.BASE_URL + "/unfinished/grouped/by-desk/" + forKitchen.toString())
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
      this.http.get(this.BASE_URL + "/unfinished/grouped/by-item/" + forKitchen.toString())
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
    return this.http.post(this.BASE_URL, orders);
  }

  /**
   * get details for a specific order
   *
   * @returns {Promise<T>}
   * @param ids
   */
  public getOrderInfos(ids: Array<string>) {
    return this.http.get(this.BASE_URL + "/" + ids.join(",")).toPromise();
  }

  /**
   * cancel orders by ids
   *
   * @param orderIds
   * @returns {Observable<Response>}
   * @param reason
   */
  public cancelOrders(orderIds: Array<string>, reason: string) {
    return this.http.put(this.BASE_URL + "/cancel?ids=" + orderIds.join(",") + "&reason=" + encodeURI(reason), {});
  }

  /**
   * switch desk orders
   *
   * @param deskIdTo
   * @param orderIds
   * @returns {Promise<T>}
   */
  switchDesk(deskIdTo: string, orderIds: Array<string>) {
    return this.http.put(this.BASE_URL + "/change/desk/" + deskIdTo + "?ids=" + orderIds.join(","), {}).toPromise();
  }


  public finishOrders(ids) {
    return this.http.put(this.BASE_URL + "/finish", ids);

  }


}
