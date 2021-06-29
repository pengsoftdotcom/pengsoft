import { Component } from '@angular/core';
import { UserDetailsService } from 'src/app/service/security/user-details.service';
import { HttpOptions } from 'src/app/service/support/http-options';
import { FieldUtils } from 'src/app/util/field-utils';
import { BaseComponent } from '../../support/base.component';

@Component({
    selector: 'app-change-password',
    templateUrl: './change-password.component.html',
    styleUrls: ['./change-password.component.scss']
})
export class ChangePasswordComponent extends BaseComponent {

    form: any = {};

    errors = {};

    fields = [
        FieldUtils.buildPassword({
            code: 'oldPassword',
            name: '原密码'
        }),
        FieldUtils.buildPassword({
            code: 'newPassword',
            name: '新密码'
        }),
        FieldUtils.buildPassword({
            code: 'confirmPassword',
            name: '确认密码'
        })
    ];

    constructor(private userDetails: UserDetailsService) { super(); }

    submit(options: HttpOptions): void {
        options.errors = this.errors;
        this.userDetails.changePassword(this.form.oldPassword, this.form.newPassword, this.form.confirmPassword, options);
    }

}
