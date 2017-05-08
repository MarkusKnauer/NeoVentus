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


}
