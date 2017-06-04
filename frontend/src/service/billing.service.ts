import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {CachingService} from "./caching.service";
import {BillingDto} from "../model/billing-dto";
/**
 * handling requests to the backend belonging the billings
 *
 * @author Tim Heidelbach
 * @author Dennis Thanner
 */
@Injectable()
export class BillingService extends CachingService {

  private static BASE_URL = "/api/billing";

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
        this.http.get(BillingService.BASE_URL + "/waiter/" + userId)
          .map(res => res.json())
          .subscribe(data => {
            this.saveToCache(userId, data);
            resolve(data);
          });
      });
    }
  }

  /**
   * insert billing
   *
   * @param billing
   * @returns {Promise<T>}
   */
  public insertBilling(billing: BillingDto) {
    return this.http.post(BillingService.BASE_URL, billing).toPromise();
  }

  /**
   * save todays billings to cache
   * @returns {Promise<TResult2|TResult1>}
   */
  public getTodaysBillings() {
    return this.http.get(BillingService.BASE_URL + "/today").toPromise().then((resp) => {
      this.saveToCache("today", resp.json());
    })
  }

}
