import { Component, ViewChild } from '@angular/core';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzModalService } from 'ng-zorro-antd/modal';
import { EditComponent } from 'src/app/component/support/edit/edit.component';
import { EntityComponent } from 'src/app/component/support/entity.component';
import { Field } from 'src/app/component/support/form-item/field';
import { ListComponent } from 'src/app/component/support/list/list.component';
import { CaptchaService } from 'src/app/service/basedata/captcha.service';
import { FieldUtils } from 'src/app/util/field-utils';
import { UserComponent } from '../../security/user/user.component';

@Component({
    selector: 'app-captcha',
    templateUrl: './captcha.component.html',
    styleUrls: ['./captcha.component.scss']
})
export class CaptchaComponent extends EntityComponent<CaptchaService> {

    @ViewChild('listComponent', { static: true }) listComponent: ListComponent;

    getListComponent(): ListComponent { return this.listComponent }

    @ViewChild('editComponent', { static: true }) editComponent: EditComponent;

    getEditComponent(): EditComponent { return this.editComponent }

    constructor(
        public entity: CaptchaService,
        public modal: NzModalService,
        public message: NzMessageService
    ) {
        super(entity, modal, message);
    }

    initFields(): void {
        UserComponent.prototype.initFields();
        this.fields = [
            FieldUtils.buildTextForCode({ width: 103, align: 'center', sortable: false }),
            FieldUtils.buildDatetimeForExpiredAt(),
            FieldUtils.buildText({
                code: 'user', name: '用户',
                children: UserComponent.prototype.fields
                    .filter(field => !['username', 'email', 'mpOpenId'].includes(field.code))
                    .map((field: Field) => {
                        if (field.code === 'expiredAt') {
                            delete field.filter;
                        }
                        return field;
                    })
            })
        ];
    }

    initListAction(): void {
        super.initListAction();
        this.listAction.splice(0, 1);
    }

}
