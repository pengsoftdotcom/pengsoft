import { Component, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { InputComponent } from '../input.component';

@Component({
    selector: 'app-input-cascader',
    templateUrl: './cascader.component.html',
    styleUrls: ['./cascader.component.scss']
})
export class CascaderComponent extends InputComponent implements OnInit, OnChanges {

    ngOnInit(): void {
        super.ngOnInit();
        if (!this.edit.input.lazy) {
            this.edit.input.load(this);
        }
    }

    ngOnChanges(changes: SimpleChanges): void {
        if (changes.form) {
            if (this.form[this.edit.code]) {
                const id = this.form[this.edit.code].id;
                const parentIds = this.form[this.edit.code].parentIds;
                if (parentIds) {
                    this.rawValue = parentIds.split('::').concat(id);
                } else {
                    this.rawValue = [id];
                }
            }
        }
    }

    modelChange(event: any): void {
        if (this.rawValue && this.rawValue.length > 0) {
            this.form[this.edit.code] = { id: this.rawValue[this.rawValue.length - 1] };
        } else {
            this.form[this.edit.code] = null;
        }
    }

}
