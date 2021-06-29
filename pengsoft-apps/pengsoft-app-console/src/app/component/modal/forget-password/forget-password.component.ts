import { Component, TemplateRef, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzModalService } from 'ng-zorro-antd/modal';
import { UserDetailsService } from 'src/app/service/security/user-details.service';
import { FieldUtils } from 'src/app/util/field-utils';
import { BaseComponent } from '../../support/base.component';

@Component({
    selector: 'app-forget-password',
    templateUrl: './forget-password.component.html',
    styleUrls: ['./forget-password.component.scss']
})
export class ForgetPasswordComponent extends BaseComponent {

    @ViewChild('content', { static: true }) content: TemplateRef<any>;

    fields = [
        FieldUtils.buildText({
            code: 'username',
            edit: {
                label: { visible: false },
                input: { placeholder: '录入已绑定的手机号码', prefixIcon: 'user' }
            }
        }),
        FieldUtils.buildPassword({
            code: 'password',
            edit: {
                label: { visible: false },
                input: { placeholder: '录入登录密码', prefixIcon: 'lock' }
            }
        }),
        FieldUtils.buildPassword({
            code: 'passwordConfirm',
            edit: {
                label: { visible: false },
                input: { placeholder: '再次录入密码', prefixIcon: 'lock' }
            }
        }),
        FieldUtils.buildCaptcha()
    ];

    form: any = {};

    errors: any = {};

    constructor(
        private userDetails: UserDetailsService, private message: NzMessageService, private modal: NzModalService, private router: Router
    ) {
        super();
    }

    resetPassword(): void {
        this.errors = {};

        if (!this.form.passwordConfirm) {
            this.errors.passwordConfirm = ['请录入'];
        }

        if (this.form.passwordConfirm !== this.form.password) {
            this.errors.passwordConfirm = ['密码不一致'];
        }

        if (JSON.stringify(this.errors) !== '{}') {
            return;
        }

        this.userDetails.resetPassword(this.form.username, this.form.password, this.form.captcha, {
            errors: this.errors,
            success: () => this.message.info('重置成功，将立即跳转登录！').onClose.subscribe(() => this.signIn())
        });

    }

    signIn(): void {
        this.router.navigateByUrl('sign-in');
    }

    signUp(): void {
        this.message.warning('暂未开放，敬请期待');
        // this.router.navigateByUrl('sign-up');
    }

}
