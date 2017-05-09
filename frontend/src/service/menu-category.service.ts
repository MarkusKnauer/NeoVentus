import {CachingService} from "./caching.service";
import {Injectable} from "@angular/core";
import {Http} from "@angular/http";

/**
 * menu category service
 *
 * @author Dennis Thanner
 * @version 0.0.1
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
    let req = this.http.get(MenuCategoryService.BASE_URL + "/tree");
    req.map(resp => resp.json()).subscribe((data) => {
      this.saveToCache("tree", data);
    }, (err) => {
      console.debug("Error loading category tree", err);
    });
    return req.toPromise();
  }

}
