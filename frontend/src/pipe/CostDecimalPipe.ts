import {Pipe, PipeTransform} from "@angular/core";

/**
 * pipe to convert number to decimal and replace dot with comma
 *
 * @author Dennis Thanner
 * @version 0.0.1
 */
@Pipe({
  name: "costDecimal"
})
export class CostDecimalPipe implements PipeTransform {

  transform(value: number, ...args: any[]): string {
    return value.toFixed(2).toString().replace(".", ",");
  }

}
