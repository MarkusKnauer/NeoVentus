import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {CachingService} from "./caching.service";

/**
 * handling requests to the backend belonging the desks
 *
 * @author Tim Heidelbach, Dennis Thanner
 * @version 0.0.2 added caching support - DT
 */
@Injectable()
export class DeskService extends CachingService {

  constructor(private http: Http) {
    super();
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
        this.http.get("/api/desk/")
          .map(res => res.json())
          .subscribe(data => {
            this.saveToCache("desks", data);
            resolve(data);
          });
      });
    }
  }
}
