import {StompService} from "ng2-stomp-service";
import {Injectable, isDevMode} from "@angular/core";
import {HttpService, ServiceUtils} from "./service-utils";
import {Events, Platform} from "ionic-angular";

/**
 * base socket service
 * handling connection for order socket endpoint and notification
 * @author Dennis Thanner
 */
@Injectable()
export class SocketService implements HttpService {

  BASE_URL_PREFIX = "/socket/socket-api";
  BASE_URL = this.BASE_URL_PREFIX;

  /**
   * indicator to prevent double connection of socket
   */
  private connection: Promise<{}>;

  private wsConfig = {
    host: this.BASE_URL,
    debug: isDevMode(),
    queue: {}
  };

  constructor(private _stomp: StompService, platform: Platform, events: Events) {
    ServiceUtils.initConnectionUrl(this, platform, events);
  }

  /**
   * connect to socket
   */
  public connect() {
    if (!this.connection) {
      this.wsConfig.host = this.BASE_URL;
      this._stomp.configure(this.wsConfig);
      this.connection = this._stomp.startConnect();
    }
    return this.connection;
  }

  /**
   * disconnect socket
   */
  public disconnect() {
    this._stomp.disconnect();
    this.connection = null;
  }


  get stomp(): StompService {
    return this._stomp;
  }

  set stomp(value: StompService) {
    this._stomp = value;
  }
}
