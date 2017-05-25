import {Component} from "@angular/core";
import {LocalStorageService} from "../../service/local-storage.service";
import {ViewController} from "ionic-angular";
/**
 * @author Dennis Thanner
 */
@Component({
  selector: "manage-storno-reasons",
  templateUrl: "manage-storno-reasons.html"
})
export class ManageStornoReasonsModalComponent {

  private newReason: string = "";

  private reasonCache;

  constructor(private localStorageService: LocalStorageService, private viewCtrl: ViewController) {
    localStorageService.loadStornoReasons().then(() => {
      this.reasonCache = this.localStorageService.cache[LocalStorageService.STORNO_REASONS_KEY];
    });
  }

  /**
   * add new reason to save local
   */
  addReason() {
    if (!this.reasonCache)
      this.reasonCache = [];
    this.reasonCache.push(this.newReason);
    // save to local storage
    this.localStorageService.saveCachedJsonData(LocalStorageService.STORNO_REASONS_KEY);
    this.newReason = "";
  }

  /**
   * save after reorder event
   * @param event
   */
  saveReorder(event) {
    // remove from old index
    let el = this.reasonCache.splice(event.from, 1)[0];
    // add at new index
    this.reasonCache.splice(event.to, 0, el);
    // save
    this.localStorageService.saveCachedJsonData(LocalStorageService.STORNO_REASONS_KEY);
  }

  /**
   * delete reason from list
   * @param reason
   */
  deleteReason(reason: string) {
    this.reasonCache.splice(this.reasonCache.indexOf(reason), 1);
    this.localStorageService.saveCachedJsonData(LocalStorageService.STORNO_REASONS_KEY);
  }

  /**
   * cloase madal
   */
  close() {
    this.viewCtrl.dismiss();
  }

}
