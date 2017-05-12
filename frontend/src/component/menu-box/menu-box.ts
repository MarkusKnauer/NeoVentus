import {Component, Input} from "@angular/core";

/**
 * menu box component for menu list in service display
 *
 * @author Dennis Thanner
 * @version 0.0.1
 */
@Component({
  templateUrl: "menu-box.html",
  selector: "menu-box"
})
export class MenuBoxComponent {

  @Input()
  private item;

  @Input()
  private count: number = 0;

}
