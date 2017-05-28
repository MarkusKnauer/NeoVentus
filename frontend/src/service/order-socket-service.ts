import {Injectable} from "@angular/core";
import {StompService} from "ng2-stomp-service";
import {SocketService} from "./socket.service";

/**
 * @author Dennis Thanner
 */
@Injectable()
export class OrderSocketService extends SocketService {

  private subscription: any;

  constructor(stomp: StompService) {
    super(stomp)
  }

  /**
   * subscribe to socket data
   *
   * @param cb callback function executed on data received
   * @param topic
   */
  subscribe(topic: string, cb: Function) {
    this.connect().then(() => {
      console.debug("order socket connected");

      this.subscription = this.stomp.subscribe(topic, cb);

    }).catch((err) => {
      console.debug("Error connecting to socket");
      console.debug(err)
    });
  }

  /**
   * unsubscribe current topic subscription
   */
  unsubscribe() {
    this.stomp.unsubscribe(this.subscription);
  }

}
