import { Component, OnChanges, SimpleChanges } from '@angular/core';
import { DateUtils } from 'src/app/util/date-utils';
import { InputComponent } from '../input.component';

@Component({
    selector: 'app-input-date',
    templateUrl: './date.component.html',
    styleUrls: ['./date.component.scss']
})
export class DateComponent extends InputComponent implements OnChanges {


    ngOnChanges(changes: SimpleChanges): void {
        if (changes.form) {
            if (this.form[this.edit.code]) {
                this.rawValue = DateUtils.parseDate(this.form[this.edit.code]);
            }
        }
    }

    modelChange(event: any): void {
        if (this.rawValue) {
            this.form[this.edit.code] = DateUtils.formatDate(this.rawValue);
        } else {
            this.form[this.edit.code] = null;
        }
    }

}
