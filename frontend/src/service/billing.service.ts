import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {CachingService} from "./caching.service";
import {BillingDto} from "../model/billing-dto";
import {Events, Platform} from "ionic-angular";
import {HttpService, ServiceUtils} from "./service-utils";
/**
 * handling requests to the backend belonging the billings
 *
 * @author Tim Heidelbach
 * @author Dennis Thanner
 */
@Injectable()
export class BillingService extends CachingService implements HttpService {

  BASE_URL_PREFIX = "/api/billing";
  BASE_URL = this.BASE_URL_PREFIX;

  constructor(private http: Http, events: Events, platform: Platform) {
    super();
    ServiceUtils.initConnectionUrl(this, platform, events);
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
        this.http.get(this.BASE_URL + "/waiter/" + userId)
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
    return this.http.post(this.BASE_URL, billing).toPromise();
  }

  /**
   * save todays billings to cache
   * @returns {Promise<TResult2|TResult1>}
   */
  public getTodaysBillings() {
    return this.http.get(this.BASE_URL + "/today").toPromise().then((resp) => {
      this.saveToCache("today", resp.json());
    })
  }

  /**
   * get this years quarters revenue
   *
   * @returns {Promise<T>}
   */
  public getQuartersRevenue() {
    return this.http.get(this.BASE_URL + "/stats/revenue/quarter").toPromise();
  }

  /**
   * get this months top 10 waiters
   * @returns {Promise<T>}
   */
  public getTop10Waiters() {
    return this.http.get(this.BASE_URL + "/stats/top10/waiters").toPromise();
  }

}
