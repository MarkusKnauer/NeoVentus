import {Injectable, isDevMode} from "@angular/core";
import {StompService} from "ng2-stomp-service";

/**
 * @author Dennis Thanner
 * @version 0.0.1
 */
@Injectable()
export class OrderSocketService {

  private wsConfig = {
    host: "/socket/socket-api",
    debug: isDevMode()
  };

  constructor(private stomp: StompService) {
  }

  /**
   * @returns {Promise<{}>}
   */
  private connect() {
    this.stomp.configure(this.wsConfig);

    return this.stomp.startConnect();
  }

  /**
   * subscribe to socket data
   *
   * @param cb callback function executed on data received
   */
  subscribe(cb: Function) {
    this.connect().then(() => {
      console.debug("order socket connected");

      this.stomp.subscribe("/topic/order", cb);

    }).catch((err) => {
      console.debug("Error connecting to socket");
      console.debug(err)
    });
  }

  /**
   *
   */
  public disconnect() {
    this.stomp.disconnect();
  }


}
