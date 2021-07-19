import { Location } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzModalService } from 'ng-zorro-antd/modal';
import { SwitchOrganizationComponent } from 'src/app/component/modal/switch-organization/switch-organization.component';
import { EditComponent } from 'src/app/component/support/edit/edit.component';
import { ListComponent } from 'src/app/component/support/list/list.component';
import { TreeEntityComponent } from 'src/app/component/support/tree-entity.component';
import { PostService } from 'src/app/service/basedata/post.service';
import { SecurityService } from 'src/app/service/support/security.service';
import { FieldUtils } from 'src/app/util/field-utils';

@Component({
    selector: 'app-post',
    templateUrl: './post.component.html',
    styleUrls: ['./post.component.scss']
})
export class PostComponent extends TreeEntityComponent<PostService> implements OnInit {

    @ViewChild('listComponent', { static: true }) listComponent: ListComponent;

    getListComponent(): ListComponent { return this.listComponent }

    @ViewChild('editComponent', { static: true }) editComponent: EditComponent;

    getEditComponent(): EditComponent { return this.editComponent }

    organization: any;

    constructor(
        private location: Location,
        private security: SecurityService,
        public entity: PostService,
        public modal: NzModalService,
        public message: NzMessageService
    ) {
        super(entity, modal, message);
        this.organization = this.security.userDetails.primaryOrganization;
    }

    get parentQueryParams(): any {
        if (this.organization) {
            return { 'organization.id': this.organization.id };
        }
    }

    initFields(): void {
        super.initFields();
        this.fields.splice(1, 0,
            FieldUtils.buildTreeSelect({ code: 'organization', name: '机构', list: { visible: false }, edit: { visible: false } }),
            FieldUtils.buildTextForName()
        );
    }

    initListToolbar(): void {
        super.initListToolbar();
        if (!this.security.userDetails.primaryOrganization) {
            this.listToolbar.splice(2, 0, {
                name: '切换机构',
                type: 'link',
                action: () => this.switchOrganization()
            });
        }
    }

    afterInit(): void {
        if (this.organization) {
            this.filterForm = { 'organization.id': this.organization.id };
            super.afterInit();
        } else {
            this.switchOrganization();
        }
    }

    switchOrganization(): void {
        this.modal.create({
            nzTitle: '切换机构',
            nzContent: SwitchOrganizationComponent,
            nzOnOk: component => {
                this.organization = component.form.organization;
                this.title = this.organization.name;
                this.afterInit();
            }
        });
    }

    afterEdit(): void {
        if (!this.editForm.id) {
            this.editForm.organization = this.organization;
        }
    }

    afterFilterFormReset(): void {
        this.filterForm = { 'organization.id': this.organization.id };
        super.afterFilterFormReset();
    }

}
