import {CachingService} from "./caching.service";
import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {HttpService, ServiceUtils} from "./service-utils";
import {Events, Platform} from "ionic-angular";

/**
 * menu category service
 *
 * @author Dennis Thanner
 */
@Injectable()
export class MenuCategoryService extends CachingService implements HttpService {

  BASE_URL_PREFIX = "/api/menu-category";
  BASE_URL = this.BASE_URL_PREFIX;

  constructor(private http: Http, platform: Platform, events: Events) {
    super();
    ServiceUtils.initConnectionUrl(this, platform, events);
  }



  /**
   * load menu category tree and return promise
   */
  loadCategoryTree() {
    let cKey = "tree";

    if (this.cache[cKey]) {
      return Promise.resolve(this.cache[cKey]);
    }

    if (!this.reqs[cKey]) {
      this.reqs[cKey] = this.http.get(this.BASE_URL + "/tree").toPromise();
      this.reqs[cKey].then((req) => {
        this.saveToCache("tree", req.json());
        this.reqs[cKey] = null;
      }, (err) => {
        console.debug("Error loading category tree", err);
        this.reqs[cKey] = null;
      });
    }

    return this.reqs[cKey];
  }

}
