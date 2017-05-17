import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {CachingService} from "./caching.service";

/**
 * handling requests to the backend belonging the reservation
 *
 * @author Tim Heidelbach
 * @version 0.0.1
 */
@Injectable()
export class ReservationService extends CachingService {

  constructor(private http: Http) {
    super();
  }

  /**
   * Gets all reservations for a desk from cache or  database and eventually stores reservations in cache
   * @returns {Observable<Response>}
   */
  public getReservationsByDesk(desk: any) {

    if (this.cache["reservations"] != null) {

      return new Promise<any>(resolve => {
        resolve(this.cache["reservations"]);
      });

    } else {
      return new Promise<any>(resolve => {
        this.http.get("/api/reservation/desk/" + desk.id)
          .map(res => res.json())
          .subscribe(data => {
            this.saveToCache("reservations", data);
            resolve(data);
          });
      });
    }
  }
}
