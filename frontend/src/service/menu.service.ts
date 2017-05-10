import {Injectable} from "@angular/core";
import {CachingService} from "./caching.service";
import {Http} from "@angular/http";

/**
 * menu service class
 * for backend request
 *
 * @author Dennis Thanner
 * @version 0.0.1
 */
@Injectable()
export class MenuService extends CachingService {

  private static BASE_URL = "/api/menu";

  constructor(private http: Http) {
    super();
  }

  /**
   * load all menus
   *
   * @returns {any}
   */
  public getAll() {
    let cKey = "all";
    if (!this.reqs[cKey]) {
      this.reqs[cKey] = this.http.get(MenuService.BASE_URL).toPromise();

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

}
