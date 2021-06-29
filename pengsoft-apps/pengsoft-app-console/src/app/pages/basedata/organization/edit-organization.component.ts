import { Component, OnInit } from '@angular/core';
import { EditComponent } from 'src/app/component/support/edit/edit.component';

@Component({
    selector: 'app-edit-organization',
    templateUrl: './edit-organization.component.html',
    styleUrls: ['./edit-organization.component.scss']
})
export class EditOrganizationComponent extends EditComponent implements OnInit {

    organizationFields = [];

    adminFields = [];

    ngOnInit(): void {
        super.ngOnInit();
        const length = this.fields.length;
        this.organizationFields = this.fields.slice(0, length - 1);
        this.adminFields = this.fields[length - 1].children;
    }

}
