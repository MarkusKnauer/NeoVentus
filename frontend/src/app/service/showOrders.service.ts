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
export class ShowOrdersService {

  orderData:any;

  constructor(private http: Http) {
    this.orderData = [];
    this.http = http;
  }

  /**
   *
   * @returns {Observable<Response>}
   */
  public listOrderDesk(deskNumber: string) {
      return new Promise(resolve => {
       this.http.get("/api/order/"+deskNumber.toString())
       .map(res => res.json())
       .subscribe(order => {
          this.orderData = order;
          console.log("THIS IS FRONTEND - Received Order data-service: DeskNumber"+ deskNumber);
          console.log("THIS IS FRONTEND - Received Order data-service: Orders "+ this.orderData);
          resolve(this.orderData);
        });
      });
  }




/*
    /**
   *
   * @returns {Observable<Response>}
   */

  public getAllOrders() {

    return new Promise(resolve => {
      // We're using Angular HTTP provider to request the data,
      // then on the response, it'll map the JSON data to a parsed JS object.
      // Next, we process the data and resolve the promise with the new data.
      this.http.get("/api/order/")

        .subscribe(data => {
          // we've got back the raw data, now generate the core schedule data
          // and save the data for later reference
          this.orderData = data;
          resolve(this.orderData);
        });
    });

  }
}
