import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'categoryColor',
  standalone: true
})
export class CategoryColorPipe implements PipeTransform {

  transform(value: string): string {
    if (!value) {
      return 'color-default';
    }

    const name = value.toLowerCase();

    switch (true) {
      case name.includes('red team'):
        return 'color-red';

      case name.includes('blue team'):
        return 'color-blue';

      case name.includes('purple team'):
        return 'color-purple';

      case name.includes('hacking'):
      case name.includes('ético'):
        return 'color-yellow';

      case name.includes('software'):
      case name.includes('licencias'):
        return 'color-orange';

      default:
        return 'color-default';
    }
  }
}
