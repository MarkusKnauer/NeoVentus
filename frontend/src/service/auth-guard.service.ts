import {Injectable} from "@angular/core";
import {Http, Response} from "@angular/http";

/**
 * service for role based access
 *
 * @author Dennis Thanner
 * @version 0.0.6 added role check withous promise - DT
 *          0.0.5 promise bug fix - DT
 *          0.0.4 minor isAuthenticated() bug fix - DT
 *          0.0.3 added async support to isAuthenticated and hasRole, added hasAnyRole - DT
 *          0.0.2 minor bug fix - DT
 */
@Injectable()
export class AuthGuardService {

  private _userDetails = null;

  private userDetailPromise: Promise<Response>;

  constructor(private http: Http) {
  }

  /**
   * load the user principal
   * @returns {Subscription}
   */
  public loadUserDetails() {
    this.userDetailPromise = this.http.get("/api/user").toPromise();
    this.userDetailPromise.then(resp => {
      this._userDetails = resp.json();
      // flatten auth array
      this._userDetails.authorities = this._userDetails.authorities.map(auth => {
        return auth.authority
      });
      console.debug(this._userDetails);
    }).catch(err => {
    });
  }

  /**
   * check if role is in principal and wait for started request to finish
   *
   * @param role
   * @returns {Promise}
   */
  public hasRolePromise(role: string): Promise<boolean> {
    console.debug("Checking for role: ", role, this._userDetails, this.userDetailPromise);

    if (!this.userDetailPromise)
      return Promise.reject(false);
    return this.userDetailPromise.then(() => {
      return this.hasRole(role);
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

  }

  /**
   * check if any role is in principal and wait for started request
   *
   * @param roles
   * @returns {Promise}
   */
  public hasAnyRolePromise(roles: string[]) {
    if (!this.userDetailPromise)
      return Promise.reject(false);
    return this.userDetailPromise.then(() => {
      return this.hasAnyRole(roles);
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


  get userDetails(): any {
    return this._userDetails;
  }

  set userDetails(value: any) {
    this._userDetails = value;
  }
}
