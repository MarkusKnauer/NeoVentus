import {Injectable} from "@angular/core";
import {Storage} from "@ionic/storage";
import {Events} from "ionic-angular";
import {CachingService} from "./caching.service";

/**
 * service for storing data on local device storage
 *
 * @author Dennis Thanner
 */
@Injectable()
export class LocalStorageService extends CachingService {

  constructor(private storage: Storage, private events: Events) {
    super();
  }

  /**
   * get favorite ids from local storage
   */
  loadMenuFavoriteIds() {
    return this.storage.ready().then(() => {
      return this.storage.get("favs").then((val) => {
        this.saveToCache("favs", val ? JSON.parse(val) : []);
      })
    })
  }

  /**
   * save menu to favorites
   */
  saveMenuFavorite(menuId: string) {
    this.cache["favs"].push(menuId);
    this.storage.ready().then(() => {
      this.storage.set("favs", JSON.stringify(this.cache["favs"]));
    });
  }

  /**
   * delete menu favorite
   */
  deleteMenuFavorite(menuId: string) {
    this.cache["favs"].splice(this.cache["favs"].indexOf(menuId), 1);
    this.storage.ready().then(() => {
      this.storage.set("favs", JSON.stringify(this.cache["favs"]));
    });
  }

}
