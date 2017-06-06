import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {CachingService} from "./caching.service";
import {HttpService, ServiceUtils} from "./service-utils";
import {Events, Platform} from "ionic-angular";

/**
 * handling requests to the backend belonging the reservation
 *
 * @author Tim Heidelbach
 */
@Injectable()
export class ReservationService extends CachingService implements HttpService {

  BASE_URL_PREFIX = "/api/reservation";
  BASE_URL = this.BASE_URL_PREFIX;

  constructor(private http: Http, platform: Platform, events: Events) {
    super();
    ServiceUtils.initConnectionUrl(this, platform, events);
  }

  /**
   * Gets all reservations for a desk from cache or  database and eventually stores reservations in cache
   * @returns {Observable<Response>}
   */
  public getReservationsByDesk(desk: any) {

    if (this.cache["desk" + desk.number] != null) {
      return new Promise<any>(resolve => {
        resolve(this.cache["desk" + desk.number]);
      });

    } else {
      return new Promise<any>(resolve => {
        this.http.get(this.BASE_URL + "/desk/" + desk.id)
          .map(res => res.json())
          .subscribe(data => {
            this.saveToCache("desk" + desk.number, data);
            resolve(data);
          });
      });
    }
  }
}
