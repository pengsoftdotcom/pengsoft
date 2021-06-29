import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { BaseComponent } from '../base.component';

@Component({
    selector: 'app-button',
    templateUrl: './button.component.html',
    styleUrls: ['./button.component.scss']
})
export class ButtonComponent extends BaseComponent implements OnInit {

    @Input() name: string;

    @Input() type: 'primary' | 'default' | 'link';

    @Input() danger = false;

    @Input() divider = false;

    @Input() size: 'large' | 'small' | 'default';

    @Input() icon: string;

    @Input() disabled: any = false;

    @Output() action = new EventEmitter<any>();

    ngOnInit(): void {
        if (this.type === undefined) {
            this.type = 'default';
        }
        if (this.danger === undefined) {
            this.danger = false;
        }
        if (this.divider === undefined) {
            this.divider = false;
        }
        if (this.size === undefined) {
            this.size = 'small';
        }
    }

}
