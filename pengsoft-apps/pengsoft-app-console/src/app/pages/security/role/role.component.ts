import { Component, ViewChild } from '@angular/core';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzModalService } from 'ng-zorro-antd/modal';
import { Button } from 'src/app/component/support/button/button';
import { EditManyToManyComponent } from 'src/app/component/support/edit-many-to-many/edit-many-to-many.component';
import { EditComponent } from 'src/app/component/support/edit/edit.component';
import { ListComponent } from 'src/app/component/support/list/list.component';
import { TreeEntityComponent } from 'src/app/component/support/tree-entity.component';
import { AuthorityService } from 'src/app/service/security/authority.service';
import { RoleService } from 'src/app/service/security/role.service';
import { FieldUtils } from 'src/app/util/field-utils';

@Component({
    selector: 'app-role',
    templateUrl: './role.component.html',
    styleUrls: ['./role.component.scss']
})
export class RoleComponent extends TreeEntityComponent<RoleService> {

    @ViewChild('listComponent', { static: true }) listComponent: ListComponent;

    getListComponent(): ListComponent { return this.listComponent }

    @ViewChild('editComponent', { static: true }) editComponent: EditComponent;

    getEditComponent(): EditComponent { return this.editComponent }

    @ViewChild('editManyToManyComponent', { static: true }) editManyToManyComponent: EditManyToManyComponent;

    grantToolbar: Array<Button> = [
        {
            name: '保存', type: 'primary', size: 'default',
            authority: this.getAuthority('grantAuthorities'),
            action: () => {
                const role = this.editForm;
                const authorities = this.editManyToManyComponent.items
                    .filter(item => item.direction === 'right')
                    .map(item => item.value);
                this.entity.grantAuthorities(role, authorities, {
                    before: () => this.editManyToManyComponent.loading = true,
                    success: () => {
                        this.message.info('保存成功');
                        this.editManyToManyComponent.hide();
                    },
                    after: () => this.editManyToManyComponent.loading = false
                });
            }
        }
    ];

    constructor(
        private authority: AuthorityService,
        protected entity: RoleService,
        protected modal: NzModalService,
        protected message: NzMessageService
    ) {
        super(entity, modal, message);
    }

    initFields(): void {
        super.initFields();
        this.fields.splice(1, 0,
            FieldUtils.buildTextForCode(),
            FieldUtils.buildTextForName(),
            FieldUtils.buildTextareaForRemark()
        );

    }

    initListAction(): void {
        super.initListAction();
        this.listAction.splice(0, 0, {
            name: '分配权限',
            type: 'link',
            width: 58,
            authority: this.getAuthority('findAllRoleAuthoritiesByRole'),
            action: (row: any) => this.editGrantedAuthorities(row)
        });
    }

    editGrantedAuthorities(row: any): void {
        this.editForm = row;
        this.editManyToManyComponent.show();
        this.authority.findAll(null, {
            before: () => this.editManyToManyComponent.loading = true,
            success: (authorities: any) => {
                this.editManyToManyComponent.items =
                    authorities.map(authority => Object.assign({ title: authority.name, key: authority.id, value: authority }));
                this.entity.findAllRoleAuthoritiesByRole(row, {
                    success: (roleAuthorities: any) =>
                        this.editManyToManyComponent.targetKeys = roleAuthorities.map(roleAuthority => roleAuthority.authority.id)
                });
            },
            after: () => this.editManyToManyComponent.loading = false
        });
    }

}
