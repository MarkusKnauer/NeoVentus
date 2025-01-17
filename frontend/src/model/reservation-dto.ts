/**
 * @author Markus Knauer
 * @version 0.0.1
 */
export class ReservationDto {

  private id: string;

  private desk: Array<string>;

  private time: Date;

  private duration: number;

  private reservedBy: string;

  private reservationName: string;

  // Getter and setter

  get Id() {
    return this.id;
  }

  set Id(id: string) {
    this.id = id;
  }

  get ReservedBy() {
    return this.reservedBy;
  }

  set ReservedBy(reservedBy: string) {
    this.reservedBy = reservedBy;
  }

  get Desk() {
    return this.desk;
  }

  set Desk(desk: Array<string>) {
    this.desk = desk;
  }

  get Time() {
    return this.time;
  }

  set Time(time: Date) {
    this.time = time;
  }

  get Duration() {
    return this.duration;
  }

  set Duration(duration: number) {
    this.duration = duration;
  }

  set ReservationName(reservationName: string) {
    this.reservationName = reservationName;
  }

  get ReservationName() {
    return this.reservationName;
  }

}
