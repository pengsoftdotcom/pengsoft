import { Component, Input, OnInit } from '@angular/core';
import { BaseComponent } from '../base.component';
import { Field } from '../form-item/field';

@Component({
    selector: 'app-filter',
    templateUrl: './filter.component.html',
    styleUrls: ['./filter.component.scss']
})
export class FilterComponent extends BaseComponent implements OnInit {

    @Input() span = 12;

    @Input() form = {};

    @Input() fields: Array<Field> = [];

    ngOnInit(): void {
        const result: Array<Field> = [];
        const queue: Array<Field> = [];
        this.fields.forEach(field => {
            queue.push(field);
            while (queue.length > 0) {
                const parent = queue.shift();
                if (parent.filter && parent.list.visible) {
                    result.push(parent);
                }
                if (parent.children) {
                    parent.children.forEach(child => queue.push(child));
                }
            }
        });
        this.fields = result;
    }

}
