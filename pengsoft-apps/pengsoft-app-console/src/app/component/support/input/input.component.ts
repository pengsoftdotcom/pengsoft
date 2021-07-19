import { Component, Input, OnInit } from '@angular/core';
import { BaseComponent } from '../base.component';
import { Edit } from '../form-item/edit';

@Component({
    template: ''
})
export abstract class InputComponent extends BaseComponent implements OnInit {

    rawValue: any;

    @Input() form: any = {};

    @Input() edit: Edit;

    @Input() errors: any = {};

    disabled = false;

    placeholder = '';

    ngOnInit(): void {
        this.initDisabled();
        this.initPlaceholder();
    }

    initDisabled(): void {
        if (typeof this.edit.disabled === 'function') {
            this.disabled = this.edit.disabled(this.form, this.edit);
        } else {
            this.disabled = this.edit.disabled === true;
        }
    }

    initPlaceholder(): void {
        if (this.edit.input.placeholder) {
            this.placeholder = this.edit.input.placeholder;
        }
    }

    modelChange(event: any): void {
        // 默认空实现
    }

}
