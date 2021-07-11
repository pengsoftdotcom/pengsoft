import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NzDrawerModule } from 'ng-zorro-antd/drawer';
import { NzGridModule } from 'ng-zorro-antd/grid';
import { NzSpaceModule } from 'ng-zorro-antd/space';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { NzTabsModule } from 'ng-zorro-antd/tabs';
import { ComponentModule } from 'src/app/component/component.module';
import { BasedataRoutingModule } from './basedata-routing.module';
import { ConsumerComponent } from './consumer/consumer.component';
import { EditConsumerComponent } from './consumer/edit-consumer.component';
import { DepartmentComponent } from './department/department.component';
import { JobComponent } from './job/job.component';
import { EditAdminComponent } from './organization/edit-admin.component';
import { OrganizationComponent } from './organization/organization.component';
import { SelectAdminComponent } from './organization/select-admin.component';
import { PersonComponent } from './person/person.component';
import { PostComponent } from './post/post.component';
import { EditStaffComponent } from './staff/edit-staff.component';
import { StaffComponent } from './staff/staff.component';
import { EditSupplierComponent } from './supplier/edit-supplier.component';
import { SupplierComponent } from './supplier/supplier.component';
import { SelectSupplierComponent } from './supplier/select-supplier.component';
import { SelectConsumerComponent } from './consumer/select-consumer.component';




@NgModule({
    declarations: [
        PersonComponent,
        OrganizationComponent,
        EditAdminComponent,
        SelectAdminComponent,
        DepartmentComponent,
        PostComponent,
        JobComponent,
        StaffComponent,
        EditStaffComponent,
        SupplierComponent,
        EditSupplierComponent,
        SelectSupplierComponent,
        ConsumerComponent,
        EditConsumerComponent,
        SelectConsumerComponent
    ],
    imports: [
        CommonModule,
        FormsModule,
        BasedataRoutingModule,
        NzGridModule,
        NzTabsModule,
        NzSpaceModule,
        NzSpinModule,
        NzDrawerModule,
        ComponentModule
    ]
})
export class BasedataModule { }
