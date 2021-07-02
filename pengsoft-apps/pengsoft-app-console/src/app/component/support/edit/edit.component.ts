import { Component, Input, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { NzDrawerRef, NzDrawerService } from 'ng-zorro-antd/drawer';
import { BaseComponent } from '../base.component';
import { Button } from '../button/button';
import { Field } from '../form-item/field';

@Component({
    selector: 'app-edit',
    templateUrl: './edit.component.html',
    styleUrls: ['./edit.component.scss']
})
export class EditComponent extends BaseComponent implements OnInit {

    @Input() title = '';

    @Input() mode: 'drawer' | 'modal' = 'drawer';

    @Input() span = 24;

    @Input() form: any = {};

    @Input() fields: Array<Field> = [];

    @Input() toolbar: Array<Button> = [];

    @Input() errors: any = {};

    @Input() labelSpan: number;

    @Input() inputSpan: number;

    @ViewChild('content', { static: true }) content: TemplateRef<any>;

    formHeight = 0;

    drawerRef: NzDrawerRef;

    constructor(private drawer: NzDrawerService) {
        super();
        this.title = '编辑';
        this.width = '30%';
    }

    ngOnInit(): void {
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
        this.initFormHeight();
    }

    initFormHeight() {
        const totalHeight = window.innerHeight;
        const titleHeight = 55;
        const toolbarHeight = 65;
        this.formHeight = totalHeight - titleHeight - toolbarHeight - 1;
    }

    show(): void {
        this.drawerRef = this.drawer.create({
            nzBodyStyle: { padding: 0 },
            nzWidth: this.width,
            nzTitle: this.title,
            nzContent: this.content
        });
    }

    hide(): void {
        if (this.drawerRef) {
            this.drawerRef.close();
        }
    }

}
