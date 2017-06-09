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

    if (value == "") {
      value = "0";
    }

    value = parseFloat(value).toFixed(2);

    if (value == "-0.00") {
      value = "0.00";
    }

    return value.replace(".", ",");
  }
}
