import { Component, OnInit, ViewChild } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzModalService } from 'ng-zorro-antd/modal';
import { NzFormatEmitEvent } from 'ng-zorro-antd/tree';
import { ResetPasswordComponent } from 'src/app/component/modal/reset-password/reset-password.component';
import { SwitchOrganizationComponent } from 'src/app/component/modal/switch-organization/switch-organization.component';
import { EditComponent } from 'src/app/component/support/edit/edit.component';
import { EntityComponent } from 'src/app/component/support/entity.component';
import { Field } from 'src/app/component/support/form-item/field';
import { Option } from 'src/app/component/support/form-item/option';
import { InputComponent } from 'src/app/component/support/input/input.component';
import { ListComponent } from 'src/app/component/support/list/list.component';
import { DepartmentService } from 'src/app/service/basedata/department.service';
import { DictionaryItemService } from 'src/app/service/basedata/dictionary-item.service';
import { JobService } from 'src/app/service/basedata/job.service';
import { StaffService } from 'src/app/service/basedata/staff.service';
import { SecurityService } from 'src/app/service/support/security.service';
import { EntityUtils } from 'src/app/util/entity-utils';
import { FieldUtils } from 'src/app/util/field-utils';
import { PersonComponent } from '../person/person.component';


@Component({
    selector: 'app-staff',
    templateUrl: './staff.component.html',
    styleUrls: ['./staff.component.scss']
})
export class StaffComponent extends EntityComponent<StaffService> implements OnInit {


    @ViewChild('listComponent', { static: true }) listComponent: ListComponent;

    getListComponent(): ListComponent { return this.listComponent }

    @ViewChild('editComponent', { static: true }) editComponent: EditComponent;

    getEditComponent(): EditComponent { return this.editComponent }

    organization: any;

    department: any;

    constructor(
        private dictionaryItem: DictionaryItemService,
        private job: JobService,
        private departmentService: DepartmentService,
        private security: SecurityService,
        public entity: StaffService,
        public modal: NzModalService,
        public message: NzMessageService
    ) {
        super(entity, modal, message);
        this.allowLoadNavData = true;
        this.organization = this.security.userDetails.organization;
    }

    initFields(): void {
        PersonComponent.prototype.dictionaryItem = this.dictionaryItem;
        PersonComponent.prototype.initFields();
        this.fields = [
            FieldUtils.buildText({ code: 'person', name: '人员', children: PersonComponent.prototype.fields }),
            FieldUtils.buildTreeSelect({
                code: 'job', name: '职位',
                list: { width: 100, align: 'center' },
                edit: {
                    input: {
                        lazy: false,
                        load: (component: InputComponent) => this.job.findAll({ 'department.organization.id': this.organization.id }, {
                            before: () => component.loading = true,
                            success: (res: any) => component.edit.input.options = EntityUtils.convertListToTree(res) as Option[],
                            after: () => component.loading = false
                        })
                    }
                }
            }),
            FieldUtils.buildBoolean({
                code: 'primary', name: '主要', list: {
                    render: (field: Field, row: any, sanitizer: DomSanitizer) => {
                        if (row[field.code]) {
                            return sanitizer.bypassSecurityTrustHtml('<span style="color: #0b8235">是</span>');
                        } else {
                            return sanitizer.bypassSecurityTrustHtml('<span style="color: #ff4d4f">否</span>');
                        }
                    }
                }
            })
        ];
    }

    initListToolbar(): void {
        super.initListToolbar();
        if (!this.security.userDetails.organization) {
            this.listToolbar.splice(2, 0, {
                name: '切换机构',
                type: 'link',
                action: () => this.switchOrganization()
            });
        }
    }

    initListAction(): void {
        super.initListAction();
        this.listAction.splice(0, 0, {
            name: '重置密码',
            type: 'link',
            width: 58,
            authority: 'security::user::reset_password',
            action: (row: any) => this.resetPassword(row)
        });
        this.listToolbar.forEach(button => {
            if (button.name === '新增') {
                button.disabled = () => !this.organization;
            }
        })
    }

    afterInit(): void {
        if (this.department) {
            delete this.filterForm['department.organization.id'];
            this.filterForm['department.id'] = this.department.id;
        }
        if (this.organization) {
            delete this.filterForm['department.id'];
            this.filterForm['department.organization.id'] = this.organization.id;
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

    loadNavData() {
        if (this.allowLoadNavData) {
            this.departmentService.findAll({ 'organization.id': this.organization.id }, {
                success: (res: any) => {
                    this.navData = EntityUtils.convertListToTree(res);
                    this.navData = [{
                        key: this.organization.id,
                        title: this.organization.name,
                        value: this.organization,
                        isLeaf: !this.navData || this.navData.length === 0,
                        children: this.navData
                    }];
                }
            });
        }
    }

    nav(event: NzFormatEmitEvent): void {
        if (this.organization.id === event.node.key) {
            this.department = null;
            delete this.filterForm['department.id'];
        } else {
            this.department = event.node.origin.value;
            this.filterForm['department.id'] = this.department.id;
        }
        this.list();
    }

    afterEdit(): void {
        this.editForm.organization = this.organization;
        this.editForm.department = this.department;
    }

    afterFilterFormReset(): void {
        if (this.organization) {
            this.filterForm['department.organization.id'] = this.organization.id;
        }
        if (this.department) {
            delete this.filterForm['department.organization.id'];
            this.filterForm['department.id'] = this.department.id;
        }
        super.afterFilterFormReset();
    }

    resetPassword(row: any): void {
        this.modal.create({
            nzBodyStyle: { padding: '16px', marginBottom: '-24px' },
            nzTitle: '重置密码',
            nzContent: ResetPasswordComponent,
            nzComponentParams: {
                form: { id: row.person.user.id }
            },
            nzOnOk: component => new Promise(resolve => {
                component.submit({
                    before: () => component.loading = true,
                    success: () => {
                        this.message.info('重置成功');
                        resolve(true);
                    },
                    failure: () => resolve(false),
                    after: () => component.loading = false
                });
            })
        });
    }

}
