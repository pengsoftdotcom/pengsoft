import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ConsumerComponent } from './consumer/consumer.component';
import { DepartmentComponent } from './department/department.component';
import { JobComponent } from './job/job.component';
import { OrganizationComponent } from './organization/organization.component';
import { PersonComponent } from './person/person.component';
import { PostComponent } from './post/post.component';
import { StaffComponent } from './staff/staff.component';
import { SupplierComponent } from './supplier/supplier.component';



const routes: Routes = [
    {
        path: 'basedata',
        data: { name: '基础数据', icon: 'book' },
        children: [
            { path: 'supplier', component: SupplierComponent, data: { name: '供应商' } },
            { path: 'consumer', component: ConsumerComponent, data: { name: '客户' } },
            { path: 'organization', component: OrganizationComponent, data: { name: '机构' } },
            { path: 'department', component: DepartmentComponent, data: { code: 'basedata::department::find_all', name: '部门'/* , exclusive: 'basedata::organization::find_all'*/ } },
            { path: 'post', component: PostComponent, data: { code: 'basedata::post::find_all', name: '职务'/* , exclusive: 'basedata::organization::find_all' */ } },
            { path: 'job', component: JobComponent, data: { code: 'basedata::job::find_all', name: '职位'/* , exclusive: 'basedata::organization::find_all' */ } },
            { path: 'staff', component: StaffComponent, data: { code: 'basedata::staff::find_page', name: '员工' } },
            { path: 'person', component: PersonComponent, data: { code: 'basedata::person::find_page', name: '人员' } }
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class BasedataRoutingModule { }
