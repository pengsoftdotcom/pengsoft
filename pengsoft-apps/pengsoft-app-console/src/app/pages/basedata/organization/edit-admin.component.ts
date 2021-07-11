import { Component, Inject, OnInit } from '@angular/core';
import { EditComponent } from 'src/app/component/support/edit/edit.component';

@Component({
    selector: 'app-edit-admin',
    templateUrl: './edit-admin.component.html',
    styleUrls: ['./edit-admin.component.scss']
})
export class EditAdminComponent extends EditComponent implements OnInit {

    adminFields = [];

    ngOnInit(): void {
        super.ngOnInit();
        this.adminFields = this.fields[this.fields.length - 1].children;
    }

}
