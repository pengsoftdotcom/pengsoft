import { Component, OnChanges, SimpleChanges, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { EntityUtils } from 'src/app/util/entity-utils';
import { Option } from '../../form-item/option';
import { InputComponent } from '../input.component';

@Component({
    selector: 'app-input-select',
    templateUrl: './select.component.html',
    styleUrls: ['./select.component.scss']
})
export class SelectComponent extends InputComponent implements OnChanges {

    constructor(public sanitizer: DomSanitizer) { super(); }

    ngOnChanges(changes: SimpleChanges): void {
        if (changes.form && this.edit) {
            const value = this.form[this.edit.code];
            if (value) {
                if (this.edit.input.multiple) {
                    this.rawValue = value.split(',');
                } else {
                    this.rawValue = value;
                    if (typeof this.rawValue === 'object') {
                        const options = this.edit.input.options;
                        if (!options.find(option => EntityUtils.equals(option.value, this.rawValue))) {
                            const render = this.edit.input.optionLabelRender;
                            const option: any = {
                                label: !render ? this.rawValue.name : this.edit.input.optionLabelRender(this.edit, this.form),
                                value: this.rawValue
                            };
                            option.title = option.label;
                            option.key = option.value;
                            this.edit.input.options.push(option);
                        }
                    }
                }
            } else {
                this.rawValue = undefined;
            }
        }
    }

    compareWith(v1: any, v2: any): boolean {
        return EntityUtils.equals(v1, v2);
    }

    modelChange(event: any): void {
        let values: Array<string>;
        if (this.rawValue instanceof Array) {
            values = this.rawValue;
        } else {
            values = [this.rawValue];
        }
        if (values.length > 0) {
            if (this.edit.input.multiple) {
                this.form[this.edit.code] = values.join(',');
            } else {
                this.form[this.edit.code] = values[0];
            }
        } else {
            this.form[this.edit.code] = null;
        }
    }

    search(keywords: string): void {
        if (this.edit.input.load) {
            this.edit.input.load(this, keywords);
        }
    }

}
