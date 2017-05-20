import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {CachingService} from "./caching.service";
/**
 * handling requests to the backend belonging the billings
 *
 * @author Tim Heidelbach
 */
@Injectable()
export class BillingService extends CachingService {

  constructor(private http: Http) {
    super();
  }

  /**
   * Gets all billings belonging to a specific waiter
   * @returns {Observable<Response>}
   */
  public getBillingByWaiter(userId: string) {

    console.log("BILLINGSERVICE - User ID: " + userId);

    if (this.cache[userId] != null) {

      return new Promise<any>(resolve => {
        resolve(this.cache[userId]);
      });

    } else {
      return new Promise<any>(resolve => {
        this.http.get("/api/billing/waiter/" + userId)
          .map(res => res.json())
          .subscribe(data => {
            this.saveToCache(userId, data);
            resolve(data);
          });
      });
    }
  }

}
