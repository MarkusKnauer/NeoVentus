/**
 * caching service to cache http request results
 *
 * @author Dennis Thanner
 * @version 0.0.2 added request promise caching
 */
export abstract class CachingService {

  private _cache: Object;

  /**
   * caching request promises to prevent duplicate loading
   * while req is not resolved yet
   *
   * @type {{}}
   */
  private _reqs = {};

  constructor() {
    this._cache = {};
  }

  /**
   * save an object in the cache
   *
   * @param key
   * @param object
   */
  protected saveToCache(key: string, object: any) {
    console.debug("Saving to Cache: ", key, object);
    this._cache[key] = object;
  }

  // getter and setter

  get cache(): Object {
    return this._cache;
  }

  set cache(value: Object) {
    this._cache = value;
  }

  get reqs(): {} {
    return this._reqs;
  }

  set reqs(value: {}) {
    this._reqs = value;
  }
}
