/**
 * order model
 *
 * @author Dennis Thanner
 * @version 0.0.1
 */
// todo add multiple side dish support
export class Order {

  private _item;

  private _sideDish;

  private _wish: string;

  constructor(item, sideDish, wish: string) {
    this._item = item;
    this._sideDish = sideDish;
    this._wish = wish;
  }

  get item() {
    return this._item;
  }

  set item(value) {
    this._item = value;
  }

  get sideDish() {
    return this._sideDish;
  }

  set sideDish(value) {
    this._sideDish = value;
  }

  get wish(): string {
    return this._wish;
  }

  set wish(value: string) {
    this._wish = value;
  }
}
