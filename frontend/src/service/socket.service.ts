import {StompService} from "ng2-stomp-service";
import {Injectable, isDevMode} from "@angular/core";
@Injectable()
export class SocketService {

  /**
   * indicator to prevent double connection of socket
   */
  private connection: Promise<{}>;

  private wsConfig = {
    host: "/socket/socket-api",
    debug: isDevMode(),
    queue: {}
  };

  constructor(private _stomp: StompService) {
  }

  /**
   *
   */
  public connect() {
    if (!this.connection) {
      this._stomp.configure(this.wsConfig);
      this.connection = this._stomp.startConnect();
    }
    return this.connection;
  }

  /**
   *
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
