import { Component, OnInit, ViewChild } from '@angular/core';
import { NzDrawerRef, NzDrawerService } from 'ng-zorro-antd/drawer';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzModalService } from 'ng-zorro-antd/modal';
import { EditOneToManyComponent } from 'src/app/component/support/edit-one-to-many/edit-one-to-many.component';
import { EditComponent } from 'src/app/component/support/edit/edit.component';
import { EntityComponent } from 'src/app/component/support/entity.component';
import { Option } from 'src/app/component/support/form-item/option';
import { InputComponent } from 'src/app/component/support/input/input.component';
import { ListComponent } from 'src/app/component/support/list/list.component';
import { DictionaryItemService } from 'src/app/service/basedata/dictionary-item.service';
import { OrganizationService } from 'src/app/service/basedata/organization.service';
import { PersonService } from 'src/app/service/basedata/person.service';
import { EntityUtils } from 'src/app/util/entity-utils';
import { FieldUtils } from 'src/app/util/field-utils';
import { DepartmentComponent } from '../department/department.component';
import { PersonComponent } from '../person/person.component';
import { PostComponent } from '../post/post.component';
import { SelectAdminComponent } from './select-admin.component';

@Component({
    selector: 'app-organization',
    templateUrl: './organization.component.html',
    styleUrls: ['./organization.component.scss']
})
export class OrganizationComponent extends EntityComponent<OrganizationService> implements OnInit {

    @ViewChild('listComponent', { static: true }) listComponent: ListComponent;

    getListComponent(): ListComponent { return this.listComponent }

    @ViewChild('editComponent', { static: true }) editComponent: EditComponent;

    getEditComponent(): EditComponent { return this.editComponent }

    @ViewChild('editAdminComponent', { static: true }) editAdminComponent: EditComponent;

    editAdminToolbar = [];

    @ViewChild('departmentsComponent', { static: true }) departmentsComponent: EditOneToManyComponent;

    @ViewChild('postsComponent', { static: true }) postsComponent: EditOneToManyComponent;

    drawerRef: NzDrawerRef;

    constructor(
        public dictionaryItem: DictionaryItemService,
        public person: PersonService,
        public entity: OrganizationService,
        public modal: NzModalService,
        public message: NzMessageService,
        public drawer: NzDrawerService
    ) {
        super(entity, modal, message);
    }

    ngOnInit(): void {
        super.ngOnInit();
        this.initEditAdminAction();
    }

    initFields(): void {
        PersonComponent.prototype.dictionaryItem = this.dictionaryItem;
        PersonComponent.prototype.initFields();
        const personFields = PersonComponent.prototype.fields;
        personFields.forEach(field => {
            switch (field.code) {
                case 'nickname':
                case 'gender':
                    field.list.visible = false;
                    break;
                default: break;
            }
        });
        this.fields = [
            FieldUtils.buildTextForCode({ width: 300 }),
            FieldUtils.buildTextForName(),
            FieldUtils.buildText({ code: 'shortName', name: '简称' }),
            FieldUtils.buildCascader({
                code: 'category', name: '类别',
                list: { width: 200, align: 'center' },
                edit: {
                    required: true,
                    input: {
                        load: (component: InputComponent) => {
                            this.dictionaryItem.findAllByTypeCode('organization_category', null, {
                                before: () => component.loading = true,
                                success: (res: any) => component.edit.input.options = EntityUtils.convertListToTree(res) as Option[],
                                after: () => component.loading = false
                            });
                        }
                    }
                },
                filter: {}
            }),
            FieldUtils.buildText({ code: 'admin', name: '管理员', children: personFields, edit: { visible: false } })
        ];
    }

    initListToolbar(): void {
        super.initListToolbar();
        this.listToolbar.forEach(button => {
            switch (button.name) {
                case '刷新':
                case '搜索':
                    delete button.authority;
                    break;
                default:
                    break;
            }
        });
    }

    initListAction(): void {
        super.initListAction();
        this.listAction.splice(0, 0, {
            name: '设置管理员', type: 'link', width: 72, authority: 'basedata::organization::find_one',
            action: (row: any) => this.editAdmin(row)
        }, {
            name: '部门', type: 'link', width: 30, authority: 'basedata::department::find_all',
            action: (row: any) => this.editDepartments(row)
        }, {
            name: '职务', type: 'link', width: 30, authority: 'basedata::post::find_all',
            action: (row: any) => this.editPosts(row)
        });
    }

    initEditAdminAction(): void {
        this.editAdminToolbar = [
            { name: '保存', type: 'primary', size: 'default', action: () => this.saveAdmin(), authority: this.getAuthority('save') },
            { name: '选择', type: 'default', size: 'default', action: (row: any) => this.showSelectAdmin(), authority: 'basedata::person::find_page' },
            { name: '删除', type: 'primary', size: 'default', danger: true, action: () => this.deleteAdmin(), authority: this.getAuthority('save') }
        ];
    }

    saveAdmin(): void {
        const form = this.buildForm();
        this.entity.save(form, {
            errors: this.errors,
            before: () => this.editAdminComponent.loading = true,
            success: (res: any) => {
                this.message.info('保存成功');
                this.editAdminComponent.hide();
                if (this.drawerRef) {
                    this.hideSelectAdmin();
                }
                this.list();
            },
            after: () => this.editAdminComponent.loading = false
        });
    }

    showSelectAdmin(): void {
        this.editAdminComponent.hide();
        this.drawerRef = this.drawer.create({
            nzBodyStyle: { padding: '16px' },
            nzWidth: '30%',
            nzTitle: '选择管理员',
            nzContent: SelectAdminComponent,
            nzContentParams: { editForm: this.editForm, organizationComponent: this }
        });
    }

    hideSelectAdmin(): void {
        this.drawerRef.close();
    }

    deleteAdmin(): void {
        const form = this.buildForm();
        form.admin = null;
        this.entity.save(form, {
            errors: this.errors,
            before: () => this.editAdminComponent.loading = true,
            success: (res: any) => {
                this.message.info('删除成功');
                this.editAdminComponent.hide();
                this.list();
            },
            after: () => this.editAdminComponent.loading = false
        });
    }

    editAdmin(organization: any): void {
        this.editForm = organization;
        if (!this.editForm.admin) {
            this.editForm.admin = {};
        }
        this.editAdminComponent.show();
    }

    editDepartments(organization: any): void {
        this.departmentsComponent.component = DepartmentComponent;
        this.departmentsComponent.params = { title: organization.name, organization };
        this.departmentsComponent.show();
    }

    editPosts(organization: any): void {
        this.postsComponent.component = PostComponent;
        this.postsComponent.width = '40%';
        this.postsComponent.params = { title: organization.name, organization };
        this.postsComponent.show();
    }

}
