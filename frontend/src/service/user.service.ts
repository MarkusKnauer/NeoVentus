import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {AuthGuardService} from "./auth-guard.service";
import {HttpService, ServiceUtils} from "./service-utils";
import {Events, Platform} from "ionic-angular";

/**
 * handling requests to the backend belonging the user
 *
 * @author Dennis Thanner, Tim Heidelbach
 */
@Injectable()
export class UserService implements HttpService {

  BASE_URL_PREFIX = "";
  BASE_URL = this.BASE_URL_PREFIX;

  constructor(private http: Http, private authGuard: AuthGuardService, platform: Platform, events: Events) {
    ServiceUtils.initConnectionUrl(this, platform, events);
  }

  /**
   * login the user and if successfully load principal with roles
   *
   * @param username
   * @param password
   * @returns {Subscription}
   */
  public login(username: string, password: string) {
    return this.http.post(this.BASE_URL + "/auth/login?username=" + username.trim() + "&password=" + password, {}).toPromise().then(() => {
      return this.authGuard.loadUserDetails();
    })
  }

  public logout() {
    // console.debug("Logout should be successfull");
    return this.http.post(this.BASE_URL + "/auth/logout", {}).toPromise().then(() => {
      return this.authGuard.setUserDataNull();
    });
  }

  public getProfileDetails() {
    return new Promise<any>(resolve => {
      this.http.get(this.BASE_URL + "/api/user/profile")
        .map(res => res.json())
        .subscribe(data => {
          resolve(data);
        });
    });
  }

  /**
   * retrieve user last week tips
   * @returns {Promise<T>}
   */
  public getLastWeekTips() {
    return this.http.get(this.BASE_URL + "/api/user/tips").toPromise();
  }
}
