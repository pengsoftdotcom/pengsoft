import { Component, OnInit } from '@angular/core';
import { EditComponent } from 'src/app/component/support/edit/edit.component';

@Component({
    selector: 'app-edit-consumer',
    templateUrl: './edit-consumer.component.html',
    styleUrls: ['./edit-consumer.component.scss']
})
export class EditConsumerComponent extends EditComponent implements OnInit {

    consumerFields = [];

    ngOnInit(): void {
        super.ngOnInit();
        const length = this.fields[1].children.length;
        this.consumerFields = this.fields[1].children.slice(0, length - 1);
    }

}
