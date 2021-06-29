import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzTabChangeEvent } from 'ng-zorro-antd/tabs';
import { OAuthService } from 'src/app/service/security/oauth.service';
import { UserDetailsService } from 'src/app/service/security/user-details.service';
import { SecurityService } from 'src/app/service/support/security.service';
import { FieldUtils } from 'src/app/util/field-utils';
import { BaseComponent } from '../../support/base.component';
import { Field } from '../../support/form-item/field';

@Component({
    selector: 'app-sign-in',
    templateUrl: './sign-in.component.html',
    styleUrls: ['./sign-in.component.scss']
})
export class SignInComponent extends BaseComponent {

    form: any = {
        grant_type: 'password'
    };

    errors: any = {};

    fields1: Array<Field> = [
        FieldUtils.buildText({
            code: 'username',
            edit: {
                label: { visible: false },
                input: { placeholder: '录入账号/手机号码/邮件/身份证', prefixIcon: 'user' }
            }
        }),
        FieldUtils.buildPassword({
            code: 'password',
            edit: {
                label: { visible: false },
                input: { placeholder: '录入登录密码', prefixIcon: 'lock' }
            }
        })
    ];

    fields2: Array<Field> = [
        FieldUtils.buildText({
            code: 'username',
            edit: {
                label: { visible: false },
                input: { placeholder: '录入已绑定的手机号码', prefixIcon: 'user' }
            }
        }),
        FieldUtils.buildCaptcha()
    ];

    constructor(
        private security: SecurityService,
        private oauth: OAuthService,
        private userDetails: UserDetailsService,
        private message: NzMessageService,
        private router: Router) {
        super();
    }

    change(event: NzTabChangeEvent) {
        this.errors = {};
        if (event.index === 0) {
            this.form.grant_type = 'password';
        } else {
            this.form.grant_type = 'captcha';
        }
    }

    signIn(): void {
        this.errors = {};
        if (!this.form.username) {
            this.errors.username = ['请录入'];
        }
        if (this.form.grant_type === 'password' && !this.form.password) {
            this.errors.password = ['请录入'];
        }
        if (this.form.grant_type === 'captcha' && !this.form.captcha) {
            this.errors.captcha = ['请录入'];
        }
        if (JSON.stringify(this.errors) !== '{}') {
            return;
        }
        this.oauth.requestToken(this.form, {
            before: () => this.loading = true,
            success: (accessToken: any) => {
                this.security.accessToken = accessToken;
                this.userDetails.current({
                    before: () => this.loading = true,
                    success: (userDetails: any) => {
                        this.security.userDetails = userDetails;
                        window.location.reload();
                    },
                    after: () => this.loading = false
                });
            },
            after: () => this.loading = false
        });
    }

    signUp(): void {
        this.message.warning('暂未开放，敬请期待');
        // this.router.navigateByUrl('sign-up');
    }

    forgotPassword(): void {
        this.router.navigateByUrl('forget-password');
    }

}
