import { Component } from '@angular/core';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzModalService } from 'ng-zorro-antd/modal';
import { DictionaryItemService } from 'src/app/service/basedata/dictionary-item.service';
import { PersonService } from 'src/app/service/basedata/person.service';
import { PersonComponent } from '../person/person.component';
import { OrganizationComponent } from './organization.component';

@Component({
    selector: 'app-select-admin',
    templateUrl: './select-admin.component.html',
    styleUrls: ['./select-admin.component.scss']
})
export class SelectAdminComponent extends PersonComponent {

    organizationComponent: OrganizationComponent;

    constructor(
        public dictionaryItem: DictionaryItemService,
        public entity: PersonService,
        public modal: NzModalService,
        public message: NzMessageService
    ) {
        super(dictionaryItem, entity, modal, message);

    }

    initListToolbar(): void {
        super.initListToolbar();
        this.listToolbar.splice(2, 2);
    }

    initListAction(): void {
        this.listAction = [{
            name: '选择',
            type: 'link',
            width: 30,
            authority: 'basedata::organization::save',
            action: (row: any) => {
                this.editForm.admin = row;
                this.organizationComponent.saveAdmin();
            }
        }];
    }

}

