import {Injectable} from "@angular/core";
import {Http} from "@angular/http";

/**
 * handling requests to the backend belonging the menuItems
 *
 * @author Dominik Streif
 * @version 0.0.1
 */
@Injectable()
export class OrderItemService {

  private orderItems = null;

  constructor(private http: Http) {
  }

  /**
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
          this.orderItems = data;
          resolve(this.orderItems);
        });
    });

  }

}
