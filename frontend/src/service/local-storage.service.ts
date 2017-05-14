import {Injectable} from "@angular/core";
import {Storage} from "@ionic/storage";
import {Events} from "ionic-angular";

/**
 * service for storing data on local device storage
 *
 * @author Dennis Thanner
 * @version 0.0.1
 */
@Injectable()
export class LocalStorageService {

  constructor(private storage: Storage, private events: Events) {
  }

  /**
   * get favorite ids from local storage
   */
  getMenuFavoriteIds() {
    return this.storage.ready().then(() => {
      return this.storage.get("favs").then((val) => {
        return val ? (Array)(JSON.parse(val)) : [];
      })
    })
  }

  /**
   * save menu to favorites
   */
  saveMenuFavorite(menuId: string) {
    this.getMenuFavoriteIds().then((favIds) => {
      if (favIds instanceof Array) {
        favIds.push(menuId);

        this.storage.set("favs", JSON.stringify(favIds));
        this.events.publish("FAV_ADDED");
      }
    });
  }

}
