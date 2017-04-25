import {Injectable} from '@angular/core';
import {Http} from '@angular/http';

/**
 * service for role based access management
 */
@Injectable()
export class AuthGuardService {

  private userDetails;

  constructor(private http: Http) {
  }

  /**
   * load the user principal
   * @returns {Subscription}
   */
  public loadUserDetails() {
    return this.http.get("/api/user").subscribe(data => {
      this.userDetails = data;
      this.userDetails.authorities = this.userDetails.authorities.map(auth => {
        return auth.authority
      });
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

}
