import {Injectable} from "@angular/core";
import {Http, Response} from "@angular/http";

/**
 * service for role based access
 *
 * @author Dennis Thanner
 * @version 0.0.4 minor isAuthenticated() bug fix
 *          0.0.3 added async support to isAuthenticated and hasRole, added hasAnyRole
 *          0.0.2 minor bug fix
 */
@Injectable()
export class AuthGuardService {

  private userDetails = null;

  private userDetailsResolved: Promise<Response>;

  constructor(private http: Http) {
  }

  /**
   * load the user principal
   * @returns {Subscription}
   */
  public loadUserDetails() {
    this.userDetailsResolved = this.http.get("/api/user").toPromise();
    this.userDetailsResolved.then(resp => {
      this.userDetails = resp.json();
      // flatten auth array
      this.userDetails.authorities = this.userDetails.authorities.map(auth => {
        return auth.authority
      });
      console.debug(this.userDetails);
    })
  }

  /**
   * check if role is in principal
   *
   * @param role
   * @returns {boolean}
   */
  public hasRole(role: string): Promise<boolean> | boolean {
    console.debug("Checking for role: ", role, this.userDetails, this.userDetailsResolved);

    if (!this.userDetailsResolved)
      return false;
    return this.userDetailsResolved.then(() => {
      return this.userDetails.authorities.indexOf(role) != -1;
    });

  }

  /**
   * check if any role is in principal
   *
   * @param roles
   * @returns {boolean}
   */
  public hasAnyRole(roles: string[]) {
    console.debug("Checking for role: ", roles, this.userDetails, this.userDetailsResolved);

    if (!this.userDetailsResolved)
      return false;
    return this.userDetailsResolved.then(() => {
      let hasRole = false;
      for (let role of roles) {
        hasRole = hasRole || this.userDetails.authorities.indexOf(role) != -1;
      }
      return hasRole;
    });
  }

  /**
   * determine if user is logged in
   * @returns {boolean}
   */
  public isAuthenticated() {
    return this.userDetailsResolved == null ? false : this.userDetailsResolved.then(() => {
      return this.userDetails != null;
    });
  }

}
