import { Component, OnChanges, SimpleChanges } from '@angular/core';
import { DateUtils } from 'src/app/util/date-utils';
import { InputComponent } from '../input.component';

@Component({
    selector: 'app-input-datetime',
    templateUrl: './datetime.component.html',
    styleUrls: ['./datetime.component.scss']
})
export class DatetimeComponent extends InputComponent implements OnChanges {


    ngOnChanges(changes: SimpleChanges): void {
        if (changes.form) {
            if (this.form[this.edit.code]) {
                this.rawValue = DateUtils.parseDatetime(this.form[this.edit.code]);
            }
        }
    }

    modelChange(event: any): void {
        if (this.rawValue) {
            this.form[this.edit.code] = DateUtils.formatDateTime(this.rawValue);
        } else {
            this.form[this.edit.code] = null;
        }
    }

}
