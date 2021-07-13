import { Component } from '@angular/core';
import { NzModalService } from 'ng-zorro-antd/modal';
import { ForgetPasswordComponent } from 'src/app/component/modal/forget-password/forget-password.component';
import { BaseComponent } from 'src/app/component/support/base.component';

@Component({
    selector: 'app-forget-password',
    templateUrl: './forget-password.component.html',
    styleUrls: ['./forget-password.component.scss']
})
export class FullScreenForgetPasswordComponent extends BaseComponent {

    constructor(private modal: NzModalService) {
        super();
        this.modal.create({
            nzContent: ForgetPasswordComponent,
            nzStyle: { top: '30%' },
            nzMaskStyle: { background: '#ffffff' },
            nzWidth: 400,
            nzClosable: false,
            nzMaskClosable: false,
            nzFooter: null
        });
    }

}
