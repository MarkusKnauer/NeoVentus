/**
 * billing dto matching backend
 *
 * @author Dennis Thanner
 */
export class BillingDto {

  private id: string;

  private totalPaid: number;

  private waiter: string;

  private items: Array<string>;

  constructor(totalPaid: number, waiter: string, items: Array<string>) {
    this.totalPaid = totalPaid;
    this.waiter = waiter;
    this.items = items;
  }
}
