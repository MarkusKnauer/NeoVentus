import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {CachingService} from "./caching.service";

/**
 * handling requests to the backend belonging the orders
 *
 * @author Julian Beck, Dennis Thanner
 * @version   0.0.4 changed url for getAllOpenOrderItems - DS
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
   * Gets all orders by state new
   *
   * @param
   * @returns {Observable<Response>}
   */
  public getAllOpenOrderItems() {

    return new Promise<any>(resolve => {
      this.http.get("/api/order/all/open")
        .map(res => res.json())
        .subscribe(data => {
          this.saveToCache("open_orders", data);
          resolve(data);
        });
    });

  }

}
