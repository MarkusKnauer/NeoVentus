import {Injectable} from "@angular/core";
import {Storage} from "@ionic/storage";
import {CachingService} from "./caching.service";

/**
 * service for storing data on local device storage
 *
 * @author Dennis Thanner
 */
@Injectable()
export class LocalStorageService extends CachingService {

  public static FAV_KEY = "favs";
  public static STORNO_REASONS_KEY = "storno_reasons";
  public static CONNECTION_URL = "connection";

  public static DESK_OVERVIEW_TILEVIEW = "desk_overview_tileview";
  public static DESK_OVERVIEW_MYDESKSONLY = "desk_overview_mydesks";

  constructor(private storage: Storage) {
    super();
  }

  /**
   * get favorite ids from local storage
   */
  loadMenuFavoriteIds() {
    return this.storage.ready().then(() => {
      return this.storage.get(LocalStorageService.FAV_KEY).then((val) => {
        this.saveToCache(LocalStorageService.FAV_KEY, val ? JSON.parse(val) : []);
      })
    })
  }

  /**
   * save menu to favorites
   */
  saveMenuFavorite(menuId: string) {
    this.cache[LocalStorageService.FAV_KEY].push(menuId);
    this.saveCachedJsonData(LocalStorageService.FAV_KEY);
  }

  /**
   * delete menu favorite
   */
  deleteMenuFavorite(menuId: string) {
    this.cache[LocalStorageService.FAV_KEY].splice(this.cache[LocalStorageService.FAV_KEY].indexOf(menuId), 1);
    this.saveCachedJsonData(LocalStorageService.FAV_KEY);
  }

  /**
   * load storno reasons to cache
   * @returns {Promise<TResult2|any|any|any>|Promise<LocalForage>|Promise<TResult|LocalForage>|Promise<any|any|any>}
   */
  loadStornoReasons() {
    return this.storage.ready().then(() => {
      return this.storage.get(LocalStorageService.STORNO_REASONS_KEY).then((val) => {
        this.saveToCache(LocalStorageService.STORNO_REASONS_KEY, val ? JSON.parse(val) : []);
      })
    })
  }

  /**
   * load connection url
   * @returns {Promise<TResult2|any|any|any>}
   */
  loadConnectionUrl() {
    return this.storage.ready().then(() => {
      return this.storage.get(LocalStorageService.CONNECTION_URL).then((val) => {
        this.saveToCache(LocalStorageService.CONNECTION_URL, val);
      })
    })
  }

  /**
   * save cached data
   * @param key
   */
  saveData(key: string, data: string) {
    this.storage.ready().then(() => {
      this.storage.set(key, data);
    });
  }

  /**
   * private save cached json data
   * @param key
   */
  saveCachedJsonData(key: string) {
    this.storage.ready().then(() => {
      this.storage.set(key, JSON.stringify(this.cache[key]));
    });
  }

  saveDeskOverviewView(value: boolean) {
    this.cache[LocalStorageService.DESK_OVERVIEW_TILEVIEW] = value;
    this.saveCachedJsonData(LocalStorageService.DESK_OVERVIEW_TILEVIEW);
  }

  loadDeskOverviewView() {
    return this.storage.ready().then(() => {
      return this.storage.get(LocalStorageService.DESK_OVERVIEW_TILEVIEW).then((val) => {
        this.saveToCache(LocalStorageService.DESK_OVERVIEW_TILEVIEW, val);
      })
    })
  }

  saveDeskOverviewMyDesks(value: boolean) {
    this.cache[LocalStorageService.DESK_OVERVIEW_MYDESKSONLY] = value;
    this.saveCachedJsonData(LocalStorageService.DESK_OVERVIEW_MYDESKSONLY);
  }

  loadDeskOverviewMyDesks() {
    return this.storage.ready().then(() => {
      return this.storage.get(LocalStorageService.DESK_OVERVIEW_MYDESKSONLY).then((val) => {
        this.saveToCache(LocalStorageService.DESK_OVERVIEW_MYDESKSONLY, val);
      })
    })
  }
}
