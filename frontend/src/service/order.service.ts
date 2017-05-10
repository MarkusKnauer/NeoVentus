import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {CachingService} from "./caching.service";

/**
 * handling requests to the backend belonging the orders
 *
 * @author Julian Beck
 * @version 0.0.2
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
      this.http.get("/api/order/" + desknumber.toString())
        .map(res => res.json())
        .subscribe(order => {
          this.saveToCache("orders_desk" + desknumber.toString(), order);
          resolve(order);
        });
    });
  }

  /**
   * Gets all orders by state
   *
   * @param state
   * @returns {Observable<Response>}
   */
  public getAllOrderItemsByState(state) {

    let req = this.http.get("/api/order/all/" + state);
    req.map(resp => resp.json()).subscribe((data) => {
      this.saveToCache("orders_state_" + state, data);
    }, (err) => {
      console.debug("Error loading category tree", err);
    });
    return req.toPromise();
  }

}
