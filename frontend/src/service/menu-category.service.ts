import {CachingService} from "./caching.service";
import {Injectable} from "@angular/core";
import {Http} from "@angular/http";

/**
 * menu category service
 *
 * @author Dennis Thanner
 * @version 0.0.3 prevented menu category tree loading if exists - DT
 *          0.0.2 changed loading tree - DT
 */
@Injectable()
export class MenuCategoryService extends CachingService {

  private static BASE_URL = "/api/menu-category";

  constructor(private http: Http) {
    super();
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
      this.reqs[cKey] = this.http.get(MenuCategoryService.BASE_URL + "/tree").toPromise();
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
