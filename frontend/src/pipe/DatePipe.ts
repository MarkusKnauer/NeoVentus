import {Pipe, PipeTransform} from "@angular/core";

/**
 * Pipe to print time in format HH:mm since there is a bug in IE/Edge which ignores the default angular pipe
 * @Author Tim Heidelbach
 */
@Pipe({
  name: 'customTime'
})

export class CustomDataPipe implements PipeTransform {

  transform(value: string, args: string[]): string {
    return value.toString().split(" ")[4].substring(0, 5);
  }
}
