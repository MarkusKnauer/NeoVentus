import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {Observable} from "rxjs/Observable";
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
    return new Promise(resolve => {
      this.http.get("/api/order?deskNumber=" + desknumber.toString())
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

    return new Promise<{}>(resolve => {
      // We're using Angular HTTP provider to request the data,
      // then on the response, it'll map the JSON data to a parsed JS object.
      // Next, we process the data and resolve the promise with the new data.
      this.http.get("/api/order/all/" + state)
        .map(res => res.json())
        .subscribe(data => {
          // we've got back the raw data, now generate the core schedule data
          // and save the data for later reference
          this.saveToCache("orders_state_" + state, data);
          resolve(data);
        });
    });

  }

}
