import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { NzModalService } from 'ng-zorro-antd/modal';
import { SignInComponent } from 'src/app/component/modal/sign-in/sign-in.component';
import { BaseComponent } from 'src/app/component/support/base.component';
import { SecurityService } from 'src/app/service/support/security.service';

@Component({
    selector: 'app-sign-in',
    templateUrl: './sign-in.component.html',
    styleUrls: ['./sign-in.component.scss']
})
export class FullScreenSignInComponent extends BaseComponent {

    constructor(private security: SecurityService, private modal: NzModalService, private router: Router) {
        super();
        if (this.security.isAuthenticated()) {
            this.router.navigateByUrl('/dashboard');
        } else {
            this.modal.create({
                nzContent: SignInComponent,
                nzStyle: { top: '30%' },
                nzMaskStyle: { background: '#ffffff' },
                nzWidth: 400,
                nzClosable: false,
                nzMaskClosable: false,
                nzFooter: null,
            });
        }
    }

}
