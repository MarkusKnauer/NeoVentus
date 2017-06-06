import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {HttpService, ServiceUtils} from "./service-utils";
import {Events, Platform} from "ionic-angular";

/**
 * handling requests to the backend belonging the shifts
 *
 * @author JB
 */
@Injectable()
export class WorkingShiftService implements HttpService {

  BASE_URL_PREFIX = "/api/shift";
  BASE_URL = this.BASE_URL_PREFIX;

  constructor(private http: Http, platform: Platform, events: Events) {
    //super();
    ServiceUtils.initConnectionUrl(this, platform, events);
  }

  /**
   * Gets all desks from cache or database and eventually stores desks in cache
   * @returns {Observable<Response>}
   */
  public getAllShifts() {
    return new Promise<any>(resolve => {
      this.http.get(this.BASE_URL)
        .map(res => res.json())
        .subscribe(data => {
          resolve(data);
        });
    });
  }


  public periodShifts(date1: String, date2: String) {
    return new Promise<any>(resolve => {
      this.http.get(this.BASE_URL + "/all/" + date1 + "&" + date2)
        .map(res => res.json())
        .subscribe(data => {
          resolve(data);
        });
    });
  }

  public userPeriodShifts(username: String, date1: String, date2: String) {
    return new Promise<any>(resolve => {
      this.http.get(this.BASE_URL + "/personal/" + username + "&" + date1 + "&" + date2)
        .map(res => res.json())
        .subscribe(data => {
          resolve(data);
        });
    });
  }
}
