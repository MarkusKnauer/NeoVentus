import {StompService} from "ng2-stomp-service";
import {isDevMode} from "@angular/core";

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

  constructor(protected stomp: StompService) {
  }

  /**
   *
   */
  protected connect() {
    if (!this.connection) {
      this.stomp.configure(this.wsConfig);
      this.connection = this.stomp.startConnect();
    }
    return this.connection;
  }

  /**
   *
   */
  public disconnect() {
    this.stomp.disconnect();
    this.connection = null;
  }

}
