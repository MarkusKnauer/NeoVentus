import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {AuthGuardService} from "./auth-guard.service";

/**
 * handling requests to the backend belonging the user
 *
 * @author Dennis Thanner
 * @version 0.0.2 changed to promise
 */
@Injectable()
export class UserService {

  constructor(private http: Http, private authGuard: AuthGuardService) {
  }

  /**
   * login the user and if successfully load principal with roles
   *
   * @param username
   * @param password
   * @returns {Subscription}
   */
  public login(username: string, password: string) {
    return this.http.post("/auth/login?username=" + username + "&password=" + password, {}).toPromise().then(() => {
      return this.authGuard.loadUserDetails();
    })
  }

}
