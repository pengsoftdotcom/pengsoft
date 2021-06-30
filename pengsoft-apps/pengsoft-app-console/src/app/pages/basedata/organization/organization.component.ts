import { Component, OnInit, ViewChild } from '@angular/core';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzModalService } from 'ng-zorro-antd/modal';
import { EditOneToManyComponent } from 'src/app/component/support/edit-one-to-many/edit-one-to-many.component';
import { EditComponent } from 'src/app/component/support/edit/edit.component';
import { Option } from 'src/app/component/support/form-item/option';
import { InputComponent } from 'src/app/component/support/input/input.component';
import { ListComponent } from 'src/app/component/support/list/list.component';
import { TreeEntityComponent } from 'src/app/component/support/tree-entity.component';
import { DictionaryItemService } from 'src/app/service/basedata/dictionary-item.service';
import { OrganizationService } from 'src/app/service/basedata/organization.service';
import { EntityUtils } from 'src/app/util/entity-utils';
import { FieldUtils } from 'src/app/util/field-utils';
import { DepartmentComponent } from '../department/department.component';
import { PersonComponent } from '../person/person.component';
import { PostComponent } from '../post/post.component';

@Component({
    selector: 'app-organization',
    templateUrl: './organization.component.html',
    styleUrls: ['./organization.component.scss']
})
export class OrganizationComponent extends TreeEntityComponent<OrganizationService> implements OnInit {

    @ViewChild('listComponent', { static: true }) listComponent: ListComponent;

    getListComponent(): ListComponent { return this.listComponent }

    @ViewChild('editComponent', { static: true }) editComponent: EditComponent;

    getEditComponent(): EditComponent { return this.editComponent }

    @ViewChild('departmentsComponent', { static: true }) departmentsComponent: EditOneToManyComponent;

    @ViewChild('postsComponent', { static: true }) postsComponent: EditOneToManyComponent;

    constructor(
        private dictionaryItem: DictionaryItemService,
        protected entity: OrganizationService,
        protected modal: NzModalService,
        protected message: NzMessageService
    ) {
        super(entity, modal, message);
    }

    initFields(): void {
        super.initFields();
        PersonComponent.prototype.dictionaryItem = this.dictionaryItem;
        PersonComponent.prototype.initFields();
        PersonComponent.prototype.fields.forEach(field => {
            switch (field.code) {
                case 'nickname':
                case 'gender':
                    field.list.visible = false;
                    break;
                default: break;
            }
        });
        this.fields.splice(1, 0,
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
            FieldUtils.buildText({ code: 'admin', name: '管理员', children: PersonComponent.prototype.fields })
        );
    }

    initListAction(): void {
        super.initListAction();
        this.listAction.splice(0, 0, {
            name: '部门', type: 'link', width: 30, authority: 'basedata::department::find_all',
            action: (row: any) => this.editDepartments(row)
        }, {
            name: '职务', type: 'link', width: 30, authority: 'basedata::post::find_all',
            action: (row: any) => this.editPosts(row)
        });
    }

    beforeEdit(): void {
        super.beforeEdit();
        this.editForm.admin = {};
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
