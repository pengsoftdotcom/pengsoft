import { Component, ViewChild } from '@angular/core';
import { NzDrawerService } from 'ng-zorro-antd/drawer';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzModalService } from 'ng-zorro-antd/modal';
import { EditComponent } from 'src/app/component/support/edit/edit.component';
import { EntityComponent } from 'src/app/component/support/entity.component';
import { ListComponent } from 'src/app/component/support/list/list.component';
import { TreeEntityComponent } from 'src/app/component/support/tree-entity.component';
import { RoleService } from 'src/app/service/security/role.service';
import { RoleComponent } from './role.component';

@Component({
    selector: 'app-select-role',
    templateUrl: './select-role.component.html',
    styleUrls: ['./select-role.component.scss']
})
export class SelectRoleComponent extends TreeEntityComponent<RoleService> {

    roleComponent: RoleComponent;

    role: any;

    @ViewChild('listComponent', { static: true }) listComponent: ListComponent;

    getListComponent(): ListComponent { return this.listComponent }

    getEditComponent(): EditComponent { return null }

    constructor(
        public entity: RoleService,
        public modal: NzModalService,
        public message: NzMessageService,
        public drawer: NzDrawerService
    ) {
        super(entity, modal, message);
        this.visible = false;
    }

    initFields(): void {
        RoleComponent.prototype.initFields();
        this.fields = RoleComponent.prototype.fields;
    }

    initListToolbar(): void {
        super.initListToolbar();
        this.listToolbar.splice(2, 2);
    }

    initListAction(): void {
        this.listAction = [{
            name: '复制',
            type: 'link',
            width: 30,
            authority: 'security::role::copy_authorities',
            action: (row: any) => this.roleComponent.copyAuthorities(row, this.role)
        }];
    }

}
