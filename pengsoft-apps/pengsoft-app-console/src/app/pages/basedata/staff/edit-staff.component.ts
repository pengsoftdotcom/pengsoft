import { Component, OnInit } from '@angular/core';
import { EditComponent } from 'src/app/component/support/edit/edit.component';

@Component({
    selector: 'app-edit-staff',
    templateUrl: './edit-staff.component.html',
    styleUrls: ['./edit-staff.component.scss']
})
export class EditStaffComponent extends EditComponent implements OnInit {

    personFields = [];

    jobFields = [];

    ngOnInit(): void {
        super.ngOnInit();
        this.personFields = this.fields[0].children;
        this.jobFields = this.fields.slice(1);
    }

}
