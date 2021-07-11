import { Location } from '@angular/common';
import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzModalService } from 'ng-zorro-antd/modal';
import { SwitchOrganizationComponent } from 'src/app/component/modal/switch-organization/switch-organization.component';
import { EditOneToManyComponent } from 'src/app/component/support/edit-one-to-many/edit-one-to-many.component';
import { EditComponent } from 'src/app/component/support/edit/edit.component';
import { ListComponent } from 'src/app/component/support/list/list.component';
import { TreeEntityComponent } from 'src/app/component/support/tree-entity.component';
import { DepartmentService } from 'src/app/service/basedata/department.service';
import { SecurityService } from 'src/app/service/support/security.service';
import { FieldUtils } from 'src/app/util/field-utils';
import { JobComponent } from '../job/job.component';
import { StaffComponent } from '../staff/staff.component';


@Component({
    selector: 'app-department',
    templateUrl: './department.component.html',
    styleUrls: ['./department.component.scss']
})
export class DepartmentComponent extends TreeEntityComponent<DepartmentService> implements OnInit {

    @ViewChild('listComponent', { static: true }) listComponent: ListComponent;

    getListComponent(): ListComponent { return this.listComponent }

    @ViewChild('editComponent', { static: true }) editComponent: EditComponent;

    getEditComponent(): EditComponent { return this.editComponent }

    @ViewChild('jobsComponent', { static: true }) jobsComponent: EditOneToManyComponent;

    @ViewChild('staffComponent', { static: true }) staffComponent: EditOneToManyComponent;

    organization: any;

    constructor(
        private location: Location,
        private security: SecurityService,
        public entity: DepartmentService,
        public modal: NzModalService,
        public message: NzMessageService
    ) {
        super(entity, modal, message);
        this.organization = this.security.userDetails.organization;
    }

    get params(): any {
        if (this.organization) {
            return { 'organization.id': this.organization.id };
        }
    }

    initFields(): void {
        super.initFields();
        this.fields.splice(1, 0,
            FieldUtils.buildTreeSelect({ code: 'organization', name: '机构', list: { visible: false }, edit: { visible: false } }),
            FieldUtils.buildTextForName(),
            FieldUtils.buildText({ code: 'shortName', name: '简称' })
        );
    }

    initListAction(): void {
        super.initListAction();
        this.listAction.splice(0, 0, {
            name: '职位', type: 'link', width: 30, authority: 'basedata::job::find_all',
            action: (row: any) => this.editJobs(row)
        }, {
            name: '员工', type: 'link', width: 30, authority: 'basedata::staff::find_all',
            action: (row: any) => this.editStaff(row)
        });
    }

    afterInit(): void {
        this.filterForm = { 'organization.id': this.organization.id };
        this.editForm = { organization: this.organization };
        super.afterInit();
    }

    editJobs(department: any): void {
        this.jobsComponent.component = JobComponent;
        this.jobsComponent.width = '70%';
        this.jobsComponent.params = { title: department.name, organization: department.organization, department, allowLoadNavData: false };
        this.jobsComponent.show();
    }

    editStaff(department: any): void {
        this.staffComponent.component = StaffComponent;
        this.staffComponent.width = '70%';
        this.staffComponent.params = { title: department.name, organization: department.organization, department, allowLoadNavData: false };
        this.staffComponent.show();
    }

    afterEdit(): void {
        if (!this.editForm.id && this.organization) {
            this.editForm.organization = this.organization;
        }
    }

    afterFilterFormReset(): void {
        if (this.organization) {
            this.filterForm = { 'organization.id': this.organization.id };
        }
        this.list();
    }

}
