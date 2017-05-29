import {Injectable} from "@angular/core";
import {Http} from "@angular/http";

/**
 * handling requests to the backend belonging the shifts
 *
 * @author JB
 */
@Injectable()
export class WorkingShiftService {

  constructor(private http: Http) {
    //super();
  }

  /**
   * Gets all desks from cache or database and eventually stores desks in cache
   * @returns {Observable<Response>}
   */
  public getAllShifts() {
    return new Promise<any>(resolve => {
      this.http.get("/api/shift")
        .map(res => res.json())
        .subscribe(data => {
          resolve(data);
        });
    });
  }


  public periodShifts(date1: String, date2: String) {
    return new Promise<any>(resolve => {
      this.http.get("/api/shift/all/"+date1+"&"+date2)
        .map(res => res.json())
        .subscribe(data => {
          resolve(data);
        });
    });
  }

  public userPeriodShifts(username: String, date1: String, date2: String) {
    return new Promise<any>(resolve => {
      this.http.get("/api/shift/personal/"+username+"&"+date1+"&"+date2)
        .map(res => res.json())
        .subscribe(data => {
          resolve(data);
        });
    });
  }
}
