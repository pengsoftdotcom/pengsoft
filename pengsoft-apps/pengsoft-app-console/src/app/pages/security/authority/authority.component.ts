import { Component, ViewChild } from '@angular/core';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzModalService } from 'ng-zorro-antd/modal';
import { EditComponent } from 'src/app/component/support/edit/edit.component';
import { EntityComponent } from 'src/app/component/support/entity.component';
import { ListComponent } from 'src/app/component/support/list/list.component';
import { AuthorityService } from 'src/app/service/security/authority.service';
import { FieldUtils } from 'src/app/util/field-utils';

@Component({
    selector: 'app-authority',
    templateUrl: './authority.component.html',
    styleUrls: ['./authority.component.scss']
})
export class AuthorityComponent extends EntityComponent<AuthorityService> {

    @ViewChild('listComponent', { static: true }) listComponent: ListComponent;

    getListComponent(): ListComponent { return this.listComponent }

    @ViewChild('editComponent', { static: true }) editComponent: EditComponent;

    getEditComponent(): EditComponent { return this.editComponent }

    constructor(
        protected entity: AuthorityService,
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

}
