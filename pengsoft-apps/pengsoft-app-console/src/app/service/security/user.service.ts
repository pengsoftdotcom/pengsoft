import { Injectable } from '@angular/core';
import { EntityService } from '../support/entity.service';
import { HttpOptions } from '../support/http-options';
import { HttpService } from '../support/http.service';

@Injectable({
    providedIn: 'root'
})
export class UserService extends EntityService {

    constructor(protected http: HttpService) { super(http); }

    get modulePath(): string {
        return 'security';
    }

    get entityPath(): string {
        return 'user';
    }

    resetPassword(id: string, password: string, confirmPassword: string, options: HttpOptions): void {
        if ((password || confirmPassword) && password !== confirmPassword) {
            this.clearErrors(options.errors);
            options.errors.confirmPassword = ['密码不一致'];
            options.failure(null);
        } else {
            const body = new FormData();
            body.set('id', this.getStringValue(id));
            body.set('password', this.getStringValue(password));
            options.body = body;
            const url = this.getApiPath('reset-password');
            this.http.request('POST', url, options);
        }
    }

    grantRoles(user: any, roles: Array<any>, options: HttpOptions): void {
        const url = this.getApiPath('grant-roles');
        options.params = { 'user.id': user.id, 'role.id': roles.map(role => role.id) };
        this.http.request('POST', url, options);
    }

    setPrimaryRole(user: any, role: any, options: HttpOptions): void {
        const url = this.getApiPath('set-primary-role');
        options.params = { 'user.id': user.id, 'role.id': role.id };
        this.http.request('POST', url, options);
    }

    findAllUserRolesByUser(user: any, options: HttpOptions): void {
        const url = this.getApiPath('find-all-user-roles-by-user');
        options.params = { id: user.id };
        this.http.request('GET', url, options);
    }

}
