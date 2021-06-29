import { Component, ViewChild } from '@angular/core';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzModalService } from 'ng-zorro-antd/modal';
import { EditOneToManyComponent } from 'src/app/component/support/edit-one-to-many/edit-one-to-many.component';
import { EditComponent } from 'src/app/component/support/edit/edit.component';
import { EntityComponent } from 'src/app/component/support/entity.component';
import { ListComponent } from 'src/app/component/support/list/list.component';
import { DictionaryTypeService } from 'src/app/service/basedata/dictionary-type.service';
import { FieldUtils } from 'src/app/util/field-utils';
import { DictionaryItemComponent } from '../dictionary-item/dictionary-item.component';

@Component({
    selector: 'app-dictionary-type',
    templateUrl: './dictionary-type.component.html',
    styleUrls: ['./dictionary-type.component.scss']
})
export class DictionaryTypeComponent extends EntityComponent<DictionaryTypeService>  {

    @ViewChild('listComponent', { static: true }) listComponent: ListComponent;

    getListComponent(): ListComponent { return this.listComponent }

    @ViewChild('editComponent', { static: true }) editComponent: EditComponent;

    getEditComponent(): EditComponent { return this.editComponent }

    @ViewChild('itemsComponent', { static: true }) itemsComponent: EditOneToManyComponent;

    constructor(
        protected entity: DictionaryTypeService,
        protected modal: NzModalService,
        protected message: NzMessageService
    ) {
        super(entity, modal, message);
    }

    initFields(): void {
        this.fields = [
            FieldUtils.buildTextForCode(),
            FieldUtils.buildTextForName(),
            FieldUtils.buildTextareaForRemark()
        ];
    }

    initListAction(): void {
        super.initListAction();
        this.listAction.splice(0, 0, {
            name: '详情',
            type: 'link',
            divider: true,
            width: 47,
            authority: 'system::dictionary_item::find_all',
            action: (row: any) => this.editItems(row)
        });
    }

    editItems(row: any): void {
        this.itemsComponent.component = DictionaryItemComponent;
        this.itemsComponent.params = { title: row.name, type: row };
        this.itemsComponent.show();
    }

}
