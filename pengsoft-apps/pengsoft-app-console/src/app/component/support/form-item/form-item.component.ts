import { Component, Input, OnInit } from '@angular/core';
import { InputType } from 'src/app/enum/input-type.enum';
import { BaseComponent } from '../base.component';
import { Edit } from './edit';
import { Field } from './field';

@Component({
    selector: 'app-form-item',
    templateUrl: './form-item.component.html',
    styleUrls: ['./form-item.component.scss']
})
export class FormItemComponent extends BaseComponent implements OnInit {

    InputType = InputType;

    @Input() form = {};

    @Input() errors = {};

    @Input() field: Field;

    @Input() filterable = false;

    @Input() labelSpan: number;

    @Input() inputSpan: number;

    edit: Edit;

    code: string;

    required = false;

    label: string;

    labelVisible = true;

    inputVisible = true;

    ngOnInit(): void {
        if (!this.form) {
            this.form = {};
        }
        if (this.labelSpan) {
            this.inputSpan = 24 - this.labelSpan;
        } else {
            this.labelSpan = 4;
        }
        if (this.inputSpan) {
            this.labelSpan = 24 - this.inputSpan;
        } else {
            this.inputSpan = 20;
        }
        this.code = this.field.parentCode ? this.field.parentCode + '.' + this.field.code : this.field.code;
        if (this.filterable) {
            this.edit = this.field.filter;
            this.edit.code = this.code;
        } else {
            this.edit = this.field.edit;
            this.edit.code = this.field.code;
        }
        this.check();
        this.initVisible();
        this.initRequired();
        this.initLabel();
        this.initLabelVisible();
        this.initInputVisible();
    }

    check(): void {
        if (this.edit === undefined) {
            throw new Error('The edit is not configured.');
        }
        if (this.edit.label === undefined) {
            throw new Error('The label is not configured.');
        }
        if (this.edit.input === undefined) {
            throw new Error('The input is not configured.');
        }
    }

    initVisible(): void {
        if (typeof this.edit.visible === 'function') {
            this.visible = this.edit.visible(this.form, this.edit);
        }
        else {
            this.visible = this.edit.visible !== false;
        }
    }

    initRequired(): void {
        this.required = !this.filterable && this.edit.required;
    }

    initLabel(): void {
        this.label = this.field.name;
        if (this.edit.label.value) {
            this.label = this.edit.label.value;
        }
    }


    initLabelVisible(): void {
        this.labelVisible = this.edit.label.visible !== false;
    }

    initInputVisible(): void {
        this.inputVisible = this.edit.input.visible !== false;
    }

    get status(): string {
        if (JSON.stringify(this.errors) !== '{}' && this.errors[this.code]) {
            return 'error';
        } else {
            return null;
        }
    }

}
