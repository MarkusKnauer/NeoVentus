import {Injectable} from "@angular/core";
import {Http, Response} from "@angular/http";
import {NotificationService} from "./notification.service";
import {Events, Platform} from "ionic-angular";
import {HttpService, ServiceUtils} from "./service-utils";

/**
 * service for role based access
 *
 * @author Dennis Thanner

 */
@Injectable()
export class AuthGuardService implements HttpService {

  BASE_URL_PREFIX = "";
  BASE_URL = this.BASE_URL_PREFIX;

  private _userDetails = null;

  private userDetailPromise: Promise<Response>;

  constructor(private http: Http, private notificationService: NotificationService, events: Events, platform: Platform) {
    ServiceUtils.initConnectionUrl(this, platform, events);
  }

  /**
   * load the user principal
   * @returns {Subscription}
   */
  public loadUserDetails() {
    this.userDetailPromise = this.http.get(this.BASE_URL + "/api/user").toPromise();
    this.userDetailPromise.then(resp => {
      this._userDetails = resp.json();
      // flatten auth array
      this._userDetails.authorities = this._userDetails.authorities.map(auth => {
        return auth.authority
      });
      // start notification listener
      this.notificationService.startup(this._userDetails.name);
      console.debug(this._userDetails);
    }).catch(err => {
    });
    return this.userDetailPromise
  }

  /**
   * check if role is in principal and wait for started request to finish
   *
   * @param role
   * @returns {Promise}
   */
  public hasRolePromise(role: string) {
    console.debug("Checking for role: ", role, this._userDetails, this.userDetailPromise);

    if (!this.userDetailPromise)
      return Promise.reject(false);
    return this.userDetailPromise.then(() => {
      let r = this.hasRole(role);
      if (r)
        return Promise.resolve();
      else
        return Promise.reject(r);
    });

  }

  /**
   * check if role is in principal
   * @param role
   * @returns {boolean}
   */
  public hasRole(role: string): boolean {
    if (!this._userDetails)
      return false;
    return this._userDetails.authorities.indexOf(role) != -1;
  }

  /**
   * set the value of _userDetails
   * @param value
   */

  public setUserDataNull() {
    this._userDetails = null;
    this.userDetailPromise = null;
    this.userDetails = null;
    // stop notification listener
    this.notificationService.disconnect();
  }

  /**
   * check if any role is in principal and wait for started request
   *
   * @param roles
   * @returns {Promise}
   */
  public hasAnyRolePromise(roles: string[]) {
    console.debug("checking roles", roles, this.userDetails)
    if (!this.userDetailPromise)
      return Promise.reject(false);
    return this.userDetailPromise.then(() => {
      let r = this.hasAnyRole(roles);
      if (r)
        return Promise.resolve();
      else
        return Promise.reject(r);
    });
  }

  /**
   * check if any role is in principal
   *
   * @param roles
   * @returns {boolean}
   */
  public hasAnyRole(roles: string[]) {
    let hasRole = false;
    for (let role of roles) {
      hasRole = hasRole || this._userDetails.authorities.indexOf(role) != -1;
    }
    return hasRole;
  }


  /**
   * determine if user is logged in
   * @returns {Promise}
   */
  public isAuthenticatedPromise() {
    if (this.userDetailPromise == null)
      return Promise.reject(false);
    return this.userDetailPromise;
  }

  public isAuthenticated() {
    return this.userDetails != null;
  }

  /**
   * get user id
   *
   * @returns {string}
   */
  public getUserId(): string {
    return this._userDetails.principal.userId;
  }


  get userDetails(): any {
    return this._userDetails;
  }

  set userDetails(value: any) {
    this._userDetails = value;
  }
}
