import {SocketService} from "./socket.service";
import {Injectable} from "@angular/core";
import {LocalNotifications} from "@ionic-native/local-notifications";
import {Platform, ToastController} from "ionic-angular";

/**
 * notification service
 * @author Dennis Thanner
 */
@Injectable()
export class NotificationService {

  constructor(private socketService: SocketService, private notifications: LocalNotifications, private platform: Platform,
              private toastCtrl: ToastController) {
  }

  /**
   * start notification socket "listener"
   * @param username
   */
  public startup(username: string) {
    this.socketService.connect().then(() => {
      this.socketService.stomp.subscribe("/user/" + username + "/notification", (data) => {
        this.onNotificationData(data)
      });
    })
  }

  /**
   * on notification data from socket handler method
   *
   * @param data
   */
  public onNotificationData(data) {

    if (this.platform.is("cordova")) {

      this.notifications.hasPermission().then(enabledNotifications => {
        if (enabledNotifications) {
          this.notifications.schedule({
            title: data.message
          });
        } else {
          this.showFallbackNotification(data);
        }
      });

    } else {
      this.showFallbackNotification(data);
    }
  }

  /**
   * show notification fallback
   *
   * @param data
   */
  private showFallbackNotification(data) {
    let toast = this.toastCtrl.create({
      message: data.message,
      showCloseButton: true
    });
    toast.present();
  }

  /**
   * disconnect from socket
   *
   * @returns {Promise<{}>}
   */
  public disconnect() {
    return this.socketService.disconnect();
  }

}
