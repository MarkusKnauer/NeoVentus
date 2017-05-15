/**
 * order model
 *
 * @author Dennis Thanner
 * @version 0.0.2 added multiple side dish support
 */
export class Order {

  private _item;

  private _sideDishes: Array<any>;

  private _wish: string;

  constructor(item, sideDishes, wish: string) {
    this._item = item;
    this._sideDishes = sideDishes ? sideDishes : [];
    this._wish = wish;
  }

  get item() {
    return this._item;
  }

  set item(value) {
    this._item = value;
  }

  get sideDishes(): Array<any> {
    return this._sideDishes;
  }

  set sideDishes(value) {
    this._sideDishes = value;
  }

  get wish(): string {
    return this._wish;
  }

  set wish(value: string) {
    this._wish = value;
  }
}
