import {Injectable} from '@angular/core';
import {Http} from '@angular/http';

/**
 * service for role based access
 *
 * @author Dennis Thanner
 * @version 0.0.2 minor bug fix
 */
@Injectable()
export class AuthGuardService {

  private userDetails = null;

  constructor(private http: Http) {
  }

  /**
   * load the user principal
   * @returns {Subscription}
   */
  public loadUserDetails() {
    return this.http.get("/api/user").subscribe(resp => {
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
  public hasRole(role: string): boolean {
    if (!this.userDetails)
      return false;
    return this.userDetails.authorities.indexOf(role) != -1;
  }

  /**
   * determine if user is logged in
   * @returns {boolean}
   */
  public isAuthenticated() {
    return this.userDetails != null;
  }

}
