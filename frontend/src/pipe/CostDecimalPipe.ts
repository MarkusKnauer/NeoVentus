import {Pipe, PipeTransform} from "@angular/core";

/**
 * pipe to convert number to decimal and replace dot with comma
 *
 * @author Dennis Thanner
 */
@Pipe({
  name: "costDecimal"
})
export class CostDecimalPipe implements PipeTransform {

  transform(value: string, ...args: any[]): string {
    if (value == "")
      value = "0";
    return parseFloat(value).toFixed(2).replace(".", ",");
  }

}
