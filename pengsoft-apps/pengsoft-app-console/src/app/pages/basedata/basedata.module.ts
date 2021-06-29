import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NzGridModule } from 'ng-zorro-antd/grid';
import { NzSpaceModule } from 'ng-zorro-antd/space';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { NzTabsModule } from 'ng-zorro-antd/tabs';
import { ComponentModule } from 'src/app/component/component.module';
import { BasedataRoutingModule } from './basedata-routing.module';
import { DepartmentComponent } from './department/department.component';
import { JobComponent } from './job/job.component';
import { EditOrganizationComponent } from './organization/edit-organization.component';
import { OrganizationComponent } from './organization/organization.component';
import { PersonComponent } from './person/person.component';
import { PostComponent } from './post/post.component';
import { EditStaffComponent } from './staff/edit-staff.component';
import { StaffComponent } from './staff/staff.component';




@NgModule({
    declarations: [
        PersonComponent,
        OrganizationComponent,
        DepartmentComponent,
        PostComponent,
        JobComponent,
        StaffComponent,
        EditStaffComponent,
        EditOrganizationComponent
    ],
    imports: [
        CommonModule,
        FormsModule,
        BasedataRoutingModule,
        NzGridModule,
        NzTabsModule,
        NzSpaceModule,
        NzSpinModule,
        ComponentModule
    ]
})
export class BasedataModule { }
