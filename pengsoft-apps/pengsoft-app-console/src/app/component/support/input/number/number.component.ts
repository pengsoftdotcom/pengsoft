import { Component, OnInit } from '@angular/core';
import { InputComponent } from '../input.component';

@Component({
    selector: 'app-input-number',
    templateUrl: './number.component.html',
    styleUrls: ['./number.component.scss']
})
export class NumberComponent extends InputComponent implements OnInit {

    ngOnInit(): void {
        super.ngOnInit();
        if (!this.edit.input.mode) {
            this.edit.input.mode = 'numeric';
        }

        if (this.edit.input.min === undefined) {
            this.edit.input.min = 0;
        }
    }

}
