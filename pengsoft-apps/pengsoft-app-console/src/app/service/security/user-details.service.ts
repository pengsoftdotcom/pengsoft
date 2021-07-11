import { Injectable } from '@angular/core';
import { NzMessageService } from 'ng-zorro-antd/message';
import { BaseService } from '../support/base.service';
import { HttpOptions } from '../support/http-options';
import { HttpService } from '../support/http.service';

@Injectable({
    providedIn: 'root'
})
export class UserDetailsService extends BaseService {

    constructor(private http: HttpService, private message: NzMessageService) { super(); }

    getApiPath(action: string): string {
        return super.getApiPath('/user-details/' + action);
    }

    current(options: HttpOptions): void {
        const url = this.getApiPath('current');
        this.http.request('GET', url, options);
    }

    changePassword(oldPassword: string, newPassword: string, confirmPassword: string, options: HttpOptions): void {
        if ((newPassword || confirmPassword) && newPassword !== confirmPassword) {
            this.clearErrors(options.errors);
            options.errors.confirmPassword = ['密码不一致'];
            options.failure(null);
        } else {
            const body = new FormData();
            body.set('oldPassword', this.getStringValue(oldPassword));
            body.set('newPassword', this.getStringValue(newPassword));
            options.body = body;
            options = this.mergeSuccess(() => this.message.info('修改成功'), options);
            const url = this.getApiPath('change-password');
            this.http.request('POST', url, options);
        }
    }

    setPrimaryJob(job: any, options: HttpOptions): void {
        options.params = { id: job.id };
        const url = this.getApiPath('set-primary-job');
        this.http.request('POST', url, options);
    }

    setPrimaryRole(role: any, options: HttpOptions): void {
        options.params = { id: role.id };
        const url = this.getApiPath('set-primary-role');
        this.http.request('POST', url, options);
    }

    setOrganization(organization: any, options: HttpOptions): void {
        options.params = { id: organization.id };
        const url = this.getApiPath('set-organization');
        this.http.request('POST', url, options);
    }

    resetPassword(username: string, password: string, captcha: string, options: HttpOptions): void {
        options.body = new FormData();
        options.body.set('username', username);
        options.body.set('password', password);
        options.body.set('captcha', captcha);
        const url = this.getApiPath('reset-password');
        this.http.request('POST', url, options);
    }

    signOut(): void {
        const url = this.getApiPath('sign-out');
        this.http.request('POST', url, {});
    }

}
