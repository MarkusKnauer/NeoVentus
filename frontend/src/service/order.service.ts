import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {Observable} from "rxjs/Observable";

/**
 * handling requests to the backend belonging the orders
 *
 * @author Julian Beck
 * @version 0.0.1
 */
@Injectable()
export class OrderService {

  orderData:any;

  private orderItems;

  constructor(private http: Http) {
    this.orderData = [];
    this.http = http;
  }

  /**
   *
   * @returns {Observable<Response>}
   */
  public listOrders(paramString: string) {
      return new Promise(resolve => {
       this.http.get("/api/order"+paramString.toString())
       .map(res => res.json())
       .subscribe(order => {
          this.orderData = order;
          console.log("ShowOrdersService - Received Order data-service: Params"+ paramString);
          console.log("ShowOrdersService - Received Order data-service: Orders "+ this.orderData);
          resolve(this.orderData);
        });
      });
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
