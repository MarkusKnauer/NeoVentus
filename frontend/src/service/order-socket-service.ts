import {Injectable} from "@angular/core";
import {SocketService} from "./socket.service";

/**
 * @author Dennis Thanner
 */
@Injectable()
export class OrderSocketService {

  private subscription: any;

  constructor(private socketService: SocketService) {

  }

  /**
   * subscribe to socket data
   *
   * @param cb callback function executed on data received
   * @param topic
   */
  subscribe(topic: string, cb: Function) {
    this.socketService.connect().then(() => {
      console.debug("order socket connected");

      this.subscription = this.socketService.stomp.subscribe(topic, cb);

    }).catch((err) => {
      console.debug("Error connecting to socket");
      console.debug(err)
    });
  }

  /**
   * unsubscribe current topic subscription
   */
  unsubscribe() {
    this.socketService.stomp.unsubscribe(this.subscription);
  }

}
