import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {CachingService} from "./caching.service";
import {Events, Platform} from "ionic-angular";
import {HttpService, ServiceUtils} from "./service-utils";

/**
 * handling requests to the backend belonging the desks
 *
 * @author Tim Heidelbach, Dennis Thanner
 */
@Injectable()
export class DeskService extends CachingService implements HttpService {

  BASE_URL_PREFIX = "/api/desk";
  BASE_URL = this.BASE_URL_PREFIX;

  constructor(private http: Http, platform: Platform, events: Events) {
    super();
    ServiceUtils.initConnectionUrl(this, platform, events);
  }


  /**
   * Gets all desks from cache or database and eventually stores desks in cache
   * @returns {Observable<Response>}
   */
  public getAllDesks() {
      if (this.cache["desks"] != null) {
        return new Promise<any>(resolve => {
          resolve(this.cache["desks"]);
        });

      } else {
        return new Promise<any>(resolve => {
          this.http.get(this.BASE_URL + "/all")
            .map(res => res.json())
            .subscribe(data => {
              this.saveToCache("desks", data);
              resolve(data);
            });
        });
      }
  }

  public getAllDesksWithDetails(force?: boolean) {
    if (this.cache["desksoverview"] != null && !force) {
        return new Promise<any>(resolve => {
          resolve(this.cache["desksoverview"]);
        });

      } else {
        return new Promise<any>(resolve => {
          this.http.get(this.BASE_URL + "/overview")
            .map(res => res.json())
            .subscribe(data => {
              this.saveToCache("desksoverview", data);
              resolve(data);
            });
        });
      }
  }
}
