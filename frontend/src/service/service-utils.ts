import {Events, Platform} from "ionic-angular";
import {ApplicationEvents} from "../app/events";
import {MyApp} from "../app/app.component";

/**
 * service utilities
 * @author Dennis Thanner
 */
export class ServiceUtils {

  /**
   * initialize connection url
   */
  public static initConnectionUrl(service: HttpService, platform: Platform, events: Events) {
    // init connection url on service creation
    if (platform.is("cordova"))
      service.BASE_URL = MyApp.CONNECTION_URL + service.BASE_URL_PREFIX;

    // // change connection url after service creation
    events.subscribe(ApplicationEvents.CONNECTION_CHANGE_EVENT, (connectionUrl) => {
      if (platform.is("cordova")) {
        MyApp.CONNECTION_URL = connectionUrl;
        service.BASE_URL = MyApp.CONNECTION_URL + service.BASE_URL_PREFIX;
      }
    });
  }
}

/**
 * interface for http service
 */
export interface HttpService {

  /**
   * base url including connection string and base url prefix for service
   */
  BASE_URL: string;

  /**
   * base url prefix path like "/api/user"
   */
  BASE_URL_PREFIX: string;

}
