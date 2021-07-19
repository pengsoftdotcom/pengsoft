import { Component, ViewChild } from '@angular/core';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzModalService } from 'ng-zorro-antd/modal';
import { EditComponent } from 'src/app/component/support/edit/edit.component';
import { ListComponent } from 'src/app/component/support/list/list.component';
import { TreeEntityComponent } from 'src/app/component/support/tree-entity.component';
import { RegionService } from 'src/app/service/basedata/region.service';
import { FieldUtils } from 'src/app/util/field-utils';

@Component({
    selector: 'app-region',
    templateUrl: './region.component.html',
    styleUrls: ['./region.component.scss']
})
export class RegionComponent extends TreeEntityComponent<RegionService> {

    @ViewChild('listComponent', { static: true }) listComponent: ListComponent;

    getListComponent(): ListComponent { return this.listComponent }

    @ViewChild('editComponent', { static: true }) editComponent: EditComponent;

    getEditComponent(): EditComponent { return this.editComponent }

    constructor(
        public entity: RegionService,
        public modal: NzModalService,
        public message: NzMessageService
    ) {
        super(entity, modal, message);
    }

    get parentQueryLazy(): boolean {
        return true;
    }

    initFields(): void {
        this.fields = [
            FieldUtils.buildCascaderForRegion(this.entity, { code: 'parent', name: '上级', list: { visible: false } }),
            FieldUtils.buildTextForCode(),
            FieldUtils.buildTextForName(),
            FieldUtils.buildText({ code: 'shortName', name: '简称' }),
            FieldUtils.buildText({ code: 'index', name: '索引' }),
            FieldUtils.buildTextareaForRemark()
        ];
    }

}
