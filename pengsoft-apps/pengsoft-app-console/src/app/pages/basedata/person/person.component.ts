import { Component, ViewChild } from '@angular/core';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzModalService } from 'ng-zorro-antd/modal';
import { ResetPasswordComponent } from 'src/app/component/modal/reset-password/reset-password.component';
import { EditComponent } from 'src/app/component/support/edit/edit.component';
import { EntityComponent } from 'src/app/component/support/entity.component';
import { InputComponent } from 'src/app/component/support/input/input.component';
import { ListComponent } from 'src/app/component/support/list/list.component';
import { DictionaryItemService } from 'src/app/service/basedata/dictionary-item.service';
import { PersonService } from 'src/app/service/basedata/person.service';
import { FieldUtils } from 'src/app/util/field-utils';

@Component({
    selector: 'app-person',
    templateUrl: './person.component.html',
    styleUrls: ['./person.component.scss']
})
export class PersonComponent extends EntityComponent<PersonService> {

    @ViewChild('listComponent', { static: true }) listComponent: ListComponent;

    getListComponent(): ListComponent { return this.listComponent }

    @ViewChild('editComponent', { static: true }) editComponent: EditComponent;

    getEditComponent(): EditComponent { return this.editComponent }

    constructor(
        public dictionaryItem: DictionaryItemService,
        public entity: PersonService,
        public modal: NzModalService,
        public message: NzMessageService
    ) {
        super(entity, modal, message);
    }

    initFields(): void {
        this.fields = [
            FieldUtils.buildAvatar(),
            FieldUtils.buildTextForName({ code: 'name', name: '姓名', edit: { required: true } }),
            FieldUtils.buildText({ code: 'nickname', name: '昵称', filter: {} }),
            FieldUtils.buildSelect({
                code: 'gender', name: '性别',
                list: { width: 80, align: 'center' },
                edit: {
                    input: {
                        load: (component: InputComponent) => {
                            this.dictionaryItem.findAllByTypeCode('gender', null, {
                                before: () => component.loading = true,
                                success: (res: any) => component.edit.input.options
                                    = res.map((value: any) => Object.assign({ label: value.name, value })),
                                after: () => component.loading = false
                            });
                        }
                    }
                },
                filter: {}
            }),
            FieldUtils.buildNumber({
                code: 'mobile', name: '手机号码',
                list: { width: 140, align: 'center' },
                edit: { required: true, input: { mode: 'tel' }, disabled: (form: any) => !!form.id },
                filter: {}
            })
        ];
    }

    initListAction(): void {
        super.initListAction();
        this.listAction.splice(0, 0, {
            name: '重置密码',
            type: 'link',
            width: 58,
            authority: 'security::user::reset_password',
            action: (row: any) => this.resetPassword(row)
        });
    }

    resetPassword(row: any): void {
        this.modal.create({
            nzBodyStyle: { padding: '16px', marginBottom: '-24px' },
            nzTitle: '重置密码',
            nzContent: ResetPasswordComponent,
            nzComponentParams: {
                form: { id: row.user.id }
            },
            nzOnOk: component => new Promise(resolve => {
                component.submit({
                    before: () => component.loading = true,
                    success: () => {
                        this.message.info('重置成功');
                        resolve(true);
                    },
                    failure: () => resolve(false),
                    after: () => component.loading = false
                });
            })
        });
    }

}
