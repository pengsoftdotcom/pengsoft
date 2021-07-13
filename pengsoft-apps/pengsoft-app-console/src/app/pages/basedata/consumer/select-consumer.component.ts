import { Component } from '@angular/core';
import { NzDrawerService } from 'ng-zorro-antd/drawer';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzModalService } from 'ng-zorro-antd/modal';
import { DictionaryItemService } from 'src/app/service/basedata/dictionary-item.service';
import { OrganizationService } from 'src/app/service/basedata/organization.service';
import { PersonService } from 'src/app/service/basedata/person.service';
import { EntityUtils } from 'src/app/util/entity-utils';
import { OrganizationComponent } from '../organization/organization.component';
import { ConsumerComponent } from './consumer.component';

@Component({
    selector: 'app-select-consumer',
    templateUrl: './select-consumer.component.html',
    styleUrls: ['./select-consumer.component.scss']
})
export class SelectConsumerComponent extends OrganizationComponent {

    consumerComponent: ConsumerComponent;

    constructor(
        public dictionaryItem: DictionaryItemService,
        public person: PersonService,
        public entity: OrganizationService,
        public modal: NzModalService,
        public message: NzMessageService,
        public drawer: NzDrawerService
    ) {
        super(dictionaryItem, person, entity, modal, message, drawer);
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
            authority: 'basedata::supplier_consumer::save',
            action: (row: any) => {
                this.editForm.consumer = row;
                this.consumerComponent.save();
            }
        }];
    }

    list(): void {
        this.entity.findAllAvailableConsumers(this.editForm.supplier, {
            before: () => this.getListComponent().loading = true,
            success: (res: any) => {
                const tree = EntityUtils.convertListToTree(res);
                const list = EntityUtils.convertTreeToList(tree, node => {
                    const value = node.value;
                    value.expand = true;
                    value.loaded = true;
                    value.children = node.children;
                    const parentIds = value.parentIds ? value.parentIds + '::' + value.id : value.id;
                    value.leaf = !res.some(data => data.parentIds.startsWith(parentIds));
                    return value;
                });
                this.listData = list;
            },
            after: () => this.getListComponent().loading = false
        });
    }

}
