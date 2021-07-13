import { Component, OnInit, ViewChild } from '@angular/core';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzModalService } from 'ng-zorro-antd/modal';
import { NzFormatEmitEvent } from 'ng-zorro-antd/tree';
import { SwitchOrganizationComponent } from 'src/app/component/modal/switch-organization/switch-organization.component';
import { Button } from 'src/app/component/support/button/button';
import { EditManyToManyComponent } from 'src/app/component/support/edit-many-to-many/edit-many-to-many.component';
import { EditComponent } from 'src/app/component/support/edit/edit.component';
import { Option } from 'src/app/component/support/form-item/option';
import { InputComponent } from 'src/app/component/support/input/input.component';
import { ListComponent } from 'src/app/component/support/list/list.component';
import { TreeEntityComponent } from 'src/app/component/support/tree-entity.component';
import { DepartmentService } from 'src/app/service/basedata/department.service';
import { JobService } from 'src/app/service/basedata/job.service';
import { PostService } from 'src/app/service/basedata/post.service';
import { RoleService } from 'src/app/service/security/role.service';
import { SecurityService } from 'src/app/service/support/security.service';
import { EntityUtils } from 'src/app/util/entity-utils';
import { FieldUtils } from 'src/app/util/field-utils';

@Component({
    selector: 'app-job',
    templateUrl: './job.component.html',
    styleUrls: ['./job.component.scss']
})
export class JobComponent extends TreeEntityComponent<JobService> implements OnInit {

    @ViewChild('listComponent', { static: true }) listComponent: ListComponent;

    getListComponent(): ListComponent { return this.listComponent }

    @ViewChild('editComponent', { static: true }) editComponent: EditComponent;

    getEditComponent(): EditComponent { return this.editComponent }

    @ViewChild('jobRolesComponent', { static: true }) jobRolesComponent: EditManyToManyComponent;

    grantToolbar: Array<Button> = [
        {
            name: '保存', type: 'primary', size: 'default',
            authority: this.getAuthority('grantRoles'),
            action: () => {
                const user = this.editForm;
                const roles = this.jobRolesComponent.items.filter(item => item.direction === 'right').map(item => item.value);
                this.entity.grantRoles(user, roles, {
                    before: () => this.jobRolesComponent.loading = true,
                    success: () => this.message.info('保存成功'),
                    after: () => this.jobRolesComponent.loading = false
                });
            }
        }
    ];

    organization: any;

    department: any;

    constructor(
        private security: SecurityService,
        private role: RoleService,
        private post: PostService,
        private departmentService: DepartmentService,
        public entity: JobService,
        public modal: NzModalService,
        public message: NzMessageService
    ) {
        super(entity, modal, message);
        this.allowLoadNavData = true;
        this.organization = this.security.userDetails.organization;
    }

    get params(): any {
        if (this.department) {
            return { 'department.organization.id': this.department.organization.id };
        }
    }

    initFields(): void {
        super.initFields();
        this.fields.splice(1, 0,
            FieldUtils.buildTreeSelect({ code: 'department', name: '部门', list: { visible: false }, edit: { visible: false } }),
            FieldUtils.buildTreeSelect({
                code: 'post', name: '职务', edit: {
                    required: true,
                    input: {
                        load: (component: InputComponent) => {
                            this.post.findAll({ 'organization.id': this.organization.id }, {
                                before: () => component.loading = true,
                                success: (res: any) => component.edit.input.options = EntityUtils.convertListToTree(res) as Option[],
                                after: () => component.loading = false
                            });
                        }
                    }
                }
            }),
            FieldUtils.buildTextForName(),
            FieldUtils.buildBooleanForLocked({ code: 'departmentChief', name: '部门主管' }),
            FieldUtils.buildBooleanForLocked({ code: 'organizationChief', name: '机构主管' })
        );
    }

    initListToolbar(): void {
        super.initListToolbar();
        this.listToolbar.find(button => button.name === '新增').disabled = () => !this.department;
        if (!this.security.userDetails.organization) {
            this.listToolbar.splice(2, 0, {
                name: '切换机构',
                type: 'link',
                action: () => this.switchOrganization()
            });
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

    initListAction(): void {
        super.initListAction();
        this.listAction.splice(0, 0,
            {
                name: '分配角色', type: 'link', width: 58, authority: this.getAuthority('findAllJobRolesByJob'),
                action: (row: any) => this.editGrantedRoles(row)
            }
        );
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

    loadNavData() {
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

    editGrantedRoles(row: any): void {
        this.editForm = row;
        this.jobRolesComponent.treeData = [];
        this.jobRolesComponent.show();
        this.role.findAll(null, {
            before: () => this.jobRolesComponent.loading = true,
            success: (roles: any) => {
                this.jobRolesComponent.items = roles.map(role => Object.assign({ title: role.name, key: role.id, value: role }));
                this.entity.findAllUserRolesByUser(row, {
                    success: (userRoles: any) => {
                        this.jobRolesComponent.targetKeys = userRoles.map(userRole => userRole.role.id);
                        this.jobRolesComponent.treeData = EntityUtils.convertListToTree(roles, entity => {
                            const node = EntityUtils.convertTreeEntityToTreeNode(entity);
                            node.expanded = true;
                            node.disabled = userRoles.some(userRole => userRole.role.id === node.key);
                            node.checked = node.disabled;
                            return node;
                        });
                    }
                });
            },
            after: () => this.jobRolesComponent.loading = false
        });
    }

    afterEdit(): void {
        if (!this.editForm.id && this.department) {
            this.editForm.department = this.department;
        }
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

}
