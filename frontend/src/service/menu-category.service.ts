import {CachingService} from "./caching.service";
import {Injectable} from "@angular/core";
import {Http} from "@angular/http";

/**
 * menu category service
 *
 * @author Dennis Thanner
 * @version 0.0.2 changed loading tree
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
    if (!this.reqs["tree"]) {
      this.reqs["tree"] = this.http.get(MenuCategoryService.BASE_URL + "/tree").toPromise();
      this.reqs["tree"].then((req) => {
        this.saveToCache("tree", req.json());
        this.reqs["tree"] = null;
      }, (err) => {
        console.debug("Error loading category tree", err);
        this.reqs["tree"] = null;
      });
    }

    return this.reqs["tree"];
  }

}
