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
   *
   * @returns {Observable<Response>}
   */
  public getAllDesks() {

    return new Promise(resolve => {
      // We're using Angular HTTP provider to request the data,
      // then on the response, it'll map the JSON data to a parsed JS object.
      // Next, we process the data and resolve the promise with the new data.
      this.http.get("/api/desk/")
        .map(res => res.json())
        .subscribe(data => {
          // we've got back the raw data, now generate the core schedule data
          // and save the data for later reference
          this.saveToCache("desks", data);
          resolve(data);
        });
    });

  }
}
