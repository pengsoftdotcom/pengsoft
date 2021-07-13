import { Component, OnInit, ViewChild } from '@angular/core';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzModalService } from 'ng-zorro-antd/modal';
import { EditComponent } from 'src/app/component/support/edit/edit.component';
import { ListComponent } from 'src/app/component/support/list/list.component';
import { TreeEntityComponent } from 'src/app/component/support/tree-entity.component';
import { DictionaryItemService } from 'src/app/service/basedata/dictionary-item.service';
import { SecurityService } from 'src/app/service/support/security.service';
import { FieldUtils } from 'src/app/util/field-utils';

@Component({
    selector: 'app-dictionary-item',
    templateUrl: './dictionary-item.component.html',
    styleUrls: ['./dictionary-item.component.scss']
})
export class DictionaryItemComponent extends TreeEntityComponent<DictionaryItemService> implements OnInit {

    @ViewChild('listComponent', { static: true }) listComponent: ListComponent;

    getListComponent(): ListComponent { return this.listComponent }

    @ViewChild('editComponent', { static: true }) editComponent: EditComponent;

    getEditComponent(): EditComponent { return this.editComponent }

    type: any;

    sortable = false;

    constructor(
        private security: SecurityService,
        public entity: DictionaryItemService,
        public modal: NzModalService,
        public message: NzMessageService
    ) {
        super(entity, modal, message);
    }

    get params(): any {
        return { 'type.id': this.type.id };
    }

    initFields(): void {
        super.initFields();
        this.fields.splice(1, 0,
            FieldUtils.buildSelect({ code: 'type', name: '类型', list: { visible: false }, edit: { visible: false } }),
            FieldUtils.buildTextForCode(),
            FieldUtils.buildTextForName(),
            FieldUtils.buildTextareaForRemark()
        );
    }

    initSortable() {
        this.sortable = this.security.hasAnyAuthority(this.getAuthority('sort'));
    }

    initForm(): void {
        this.filterForm = { 'type.id': this.type.id };
        this.editForm = { type: this.type };
    }

    afterInit(): void {
        this.initForm();
        this.initSortable();
        this.list();
    }

    afterEdit(): void {
        if (!this.editForm.id) {
            this.editForm.type = this.type;
        }
    }

    afterFilterFormReset(): void {
        this.filterForm = { 'type.id': this.type.id };
        this.list();
    }

}
