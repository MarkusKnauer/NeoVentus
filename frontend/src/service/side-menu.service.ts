import {CachingService} from "./caching.service";
import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
/**
 * handling requests to the backend belonging the side-menu
 *
 * @author Markus Knauer
 * @version 0.0.1
 */

//todo: clearify if nessesary - MK

@Injectable()
export class SideDishService extends CachingService {

  constructor(private http: Http) {
    super();
  }

  /**
   *
   * @returns {Observable<Response>}
   */
  public getUser() {

    if (this.cache["user"] != null) {

      console.log("DANGER! THIS IS FRONTEND! - using cache for users");
      return new Promise<any>(resolve => {
        resolve(this.cache["user"]);
      });

    } else {

      return new Promise<any>(resolve => {
        this.http.get("/api/user/")
          .map(res => res.json())
          .subscribe(data => {
            this.saveToCache("user", data);
            resolve(data);
          });
      });
    }
  }
}
