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

    let time = new Date(value);

    let hours = time.getHours().toString();
    let minutes = time.getMinutes().toString();

    hours.length == 1 ? hours = "0" + hours : hours;
    minutes.length == 1 ? minutes = "0" + minutes : minutes;

    return hours + ":" + minutes;
  }
}
