import { Component, ViewChild } from '@angular/core';
import { NzDrawerRef, NzDrawerService } from 'ng-zorro-antd/drawer';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzModalService } from 'ng-zorro-antd/modal';
import { SwitchOrganizationComponent } from 'src/app/component/modal/switch-organization/switch-organization.component';
import { EditComponent } from 'src/app/component/support/edit/edit.component';
import { EntityComponent } from 'src/app/component/support/entity.component';
import { ListComponent } from 'src/app/component/support/list/list.component';
import { DictionaryItemService } from 'src/app/service/basedata/dictionary-item.service';
import { OrganizationService } from 'src/app/service/basedata/organization.service';
import { SupplierConsumerService } from 'src/app/service/basedata/supplier-consumer.service';
import { SecurityService } from 'src/app/service/support/security.service';
import { FieldUtils } from 'src/app/util/field-utils';
import { OrganizationComponent } from '../organization/organization.component';
import { SelectConsumerComponent } from './select-consumer.component';

@Component({
    selector: 'app-consumer',
    templateUrl: './consumer.component.html',
    styleUrls: ['./consumer.component.scss']
})
export class ConsumerComponent extends EntityComponent<SupplierConsumerService> {

    @ViewChild('listComponent', { static: true }) listComponent: ListComponent;

    getListComponent(): ListComponent { return this.listComponent }

    @ViewChild('editComponent', { static: true }) editComponent: EditComponent;

    getEditComponent(): EditComponent { return this.editComponent }

    drawerRef: NzDrawerRef;

    supplier: any;

    constructor(
        private security: SecurityService,
        private dictionaryItem: DictionaryItemService,
        private organization: OrganizationService,
        public entity: SupplierConsumerService,
        public modal: NzModalService,
        public message: NzMessageService,
        public drawer: NzDrawerService
    ) {
        super(entity, modal, message);
        this.supplier = this.security.userDetails.organization;
    }

    initFields(): void {
        OrganizationComponent.prototype.dictionaryItem = this.dictionaryItem;
        OrganizationComponent.prototype.entity = this.organization;
        OrganizationComponent.prototype.initFields();
        const organizationFields = OrganizationComponent.prototype.fields;
        this.fields = [
            FieldUtils.buildSelect({ code: 'supplier', name: '供应商', list: { visible: false, childrenVisible: false }, edit: { visible: false, childrenVisible: false } }),
            FieldUtils.buildSelect({ code: 'consumer', name: '客户', children: organizationFields, list: { visible: false } })
        ];
    }

    afterInit(): void {
        if (this.supplier) {
            this.filterForm['supplier.id'] = this.supplier.id;
            super.afterInit();
        } else {
            this.switchOrganization();
        }
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
        this.listToolbar.forEach(button => {
            if (button.name === '新增') {
                button.disabled = () => !this.supplier;
            }
        })
    }

    initEditToolbar(): void {
        super.initEditToolbar();
        this.editToolbar.splice(1, 0, { name: '选择', type: 'default', size: 'default', action: (row: any) => this.showSelectSupplier() });
    }

    afterEdit(): void {
        this.editForm.supplier = this.supplier;
    }

    save(): void {
        const form = this.buildForm();
        this.entity.saveConsumer(form, {
            errors: this.errors,
            before: () => this.getEditComponent().loading = true,
            success: (res: any) => this.saveSuccess(res),
            after: () => this.getEditComponent().loading = false
        });
    }

    saveSuccess(res: any): void {
        super.saveSuccess(res);
        if (this.drawerRef) {
            this.hideSelectSupplier();
        }
    }

    showSelectSupplier(): void {
        this.editComponent.hide();
        this.drawerRef = this.drawer.create({
            nzBodyStyle: { padding: '16px' },
            nzWidth: '70%',
            nzTitle: '选择客户',
            nzContent: SelectConsumerComponent,
            nzContentParams: { editForm: this.editForm, consumerComponent: this }
        });
    }

    hideSelectSupplier(): void {
        this.drawerRef.close();
    }

    switchOrganization(): void {
        this.modal.create({
            nzTitle: '切换机构',
            nzContent: SwitchOrganizationComponent,
            nzOnOk: component => {
                this.supplier = component.form.organization;
                this.title = this.supplier.shortName;
                this.afterInit();
            }
        });
    }

}
