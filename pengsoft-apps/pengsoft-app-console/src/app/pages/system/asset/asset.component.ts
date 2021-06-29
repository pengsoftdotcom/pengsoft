import { Component, TemplateRef, ViewChild } from '@angular/core';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzModalService } from 'ng-zorro-antd/modal';
import { EditComponent } from 'src/app/component/support/edit/edit.component';
import { EntityComponent } from 'src/app/component/support/entity.component';
import { ListComponent } from 'src/app/component/support/list/list.component';
import { AssetService } from 'src/app/service/basedata/asset.service';
import { FieldUtils } from 'src/app/util/field-utils';

@Component({
    selector: 'app-asset',
    templateUrl: './asset.component.html',
    styleUrls: ['./asset.component.scss']
})
export class AssetComponent extends EntityComponent<AssetService> {

    @ViewChild('listComponent', { static: true }) listComponent: ListComponent;

    getListComponent(): ListComponent { return this.listComponent }

    @ViewChild('editComponent', { static: true }) editComponent: EditComponent;

    getEditComponent(): EditComponent { return this.editComponent }

    @ViewChild('content', { static: true }) content: TemplateRef<any>;

    constructor(
        protected entity: AssetService,
        protected modal: NzModalService,
        protected message: NzMessageService) {
        super(entity, modal, message);
    }

    initFields(): void {
        this.fields = [
            FieldUtils.buildText({
                code: 'originalName', name: '原名称',
                edit: { disabled: true },
                filter: { disabled: false }
            }),
            FieldUtils.buildText({
                code: 'presentName', name: '现名称',
                list: { visible: false },
                edit: { disabled: true },
                filter: { disabled: false }
            }),
            FieldUtils.buildText({ code: 'storagePath', name: '存储地址', list: { visible: false }, edit: { disabled: true } }),
            FieldUtils.buildText({ code: 'accessPath', name: '访问地址', list: { visible: false }, edit: { disabled: true } }),
            FieldUtils.buildText({ code: 'contentType', name: 'MIME类型', edit: { disabled: true } }),
            FieldUtils.buildNumber({
                code: 'contentLength', name: '大小(B)', edit: { disabled: true },
                filter: { disabled: false, input: { placeholder: '小于录入的值' } }
            }),
            FieldUtils.buildBooleanForLocked(),
        ];
    }

    initListToolbar(): void {
        super.initListToolbar();
        this.listToolbar.splice(2, 1, {
            name: '上传',
            type: 'primary',
            authority: this.getAuthority('upload'),
            action: () => {
                this.modal.create({
                    nzTitle: '上传',
                    nzContent: this.content,
                    nzCancelText: null,
                    nzOnOk: () => this.list()
                });
            }
        });
    }

    initListAction(): void {
        super.initListAction();
        this.listAction[0].name = '查看';
    }

    intEditToolbar(): void {
        this.editToolbar = [];
    }

}
