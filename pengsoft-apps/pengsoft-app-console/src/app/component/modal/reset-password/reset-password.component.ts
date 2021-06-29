import { Component } from '@angular/core';
import { UserService } from 'src/app/service/security/user.service';
import { HttpOptions } from 'src/app/service/support/http-options';
import { FieldUtils } from 'src/app/util/field-utils';
import { BaseComponent } from '../../support/base.component';

@Component({
    selector: 'app-reset-password',
    templateUrl: './reset-password.component.html',
    styleUrls: ['./reset-password.component.scss']
})
export class ResetPasswordComponent extends BaseComponent {

    form: any = {};

    errors = {};

    fields = [
        FieldUtils.buildPassword({
            code: 'password',
            name: '密码'
        }),
        FieldUtils.buildPassword({
            code: 'confirmPassword',
            name: '确认密码'
        })
    ];

    constructor(private user: UserService) { super(); }

    submit(options: HttpOptions): void {
        options.errors = this.errors;
        this.user.resetPassword(this.form.id, this.form.password, this.form.confirmPassword, options);
    }

}
