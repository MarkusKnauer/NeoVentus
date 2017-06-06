import {Injectable} from "@angular/core";
import {CachingService} from "./caching.service";
import {Http} from "@angular/http";
import {HttpService, ServiceUtils} from "./service-utils";
import {Events, Platform} from "ionic-angular";

/**
 * menu service class
 * for backend request
 *
 * @author Dennis Thanner
 */
@Injectable()
export class MenuService extends CachingService implements HttpService {

  BASE_URL_PREFIX = "/api/menu";
  BASE_URL = this.BASE_URL_PREFIX;

  constructor(private http: Http, platform: Platform, events: Events) {
    super();
    ServiceUtils.initConnectionUrl(this, platform, events);
  }

  /**
   * load all menus
   *
   * @returns {any}
   */
  public getAll() {
    let cKey = "all";
    if (this.cache[cKey]) {
      return Promise.resolve(this.cache[cKey]);
    }

    if (!this.reqs[cKey]) {
      this.reqs[cKey] = this.http.get(this.BASE_URL + "/all").toPromise();

      this.reqs[cKey].then((resp) => {
        this.saveToCache(cKey, resp.json());
        // delete req
        this.reqs[cKey] = null;
      }, (resp) => {
        console.debug("Error loading menu", resp);
        this.reqs[cKey] = null;
      })
    }
    return this.reqs[cKey];
  }

  /**
   * get the most popular guest wishes
   *
   * @param id
   * @returns {Observable<Response>}
   */
  public getPopularGuestWishes(id: string) {
    return this.http.get(this.BASE_URL + "/popular-wishes/" + id).toPromise();
  }

  /**
   * get processing details
   *
   * @param id
   * @returns {Promise<T>}
   */
  public getProcessingDetails(id: string) {
    return this.http.get(this.BASE_URL + "/processing-details/" + id).toPromise();
  }

}
