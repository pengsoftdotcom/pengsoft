import { Component, OnInit } from '@angular/core';
import { EditComponent } from 'src/app/component/support/edit/edit.component';

@Component({
    selector: 'app-edit-supplier',
    templateUrl: './edit-supplier.component.html',
    styleUrls: ['./edit-supplier.component.scss']
})
export class EditSupplierComponent extends EditComponent implements OnInit {

    supplierFields = [];

    ngOnInit(): void {
        super.ngOnInit();
        const length = this.fields[0].children.length;
        this.supplierFields = this.fields[0].children.slice(0, length - 1);

    }

}
