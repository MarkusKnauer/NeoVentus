/**
 * order dto matching backend
 *
 * @author Dennis Thanner
 * @version 0.0.1
 */
export class OrderDto {

  private deskNumber: number;

  private waiter: number;

  private guestWish: string;

  private sideDishIds: Array<string>;

  private menuItemNumber: string;

  // constructor

  constructor(deskNumber: number, waiter: number, guestWish: string, sideDishIds: Array<string>, menuItemNumber: string) {
    this.deskNumber = deskNumber;
    this.waiter = waiter;
    this.guestWish = guestWish;
    this.sideDishIds = sideDishIds;
    this.menuItemNumber = menuItemNumber;
  }
}
