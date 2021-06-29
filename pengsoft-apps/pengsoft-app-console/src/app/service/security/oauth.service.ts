import { Injectable } from '@angular/core';
import { BaseService } from '../support/base.service';
import { HttpOptions } from '../support/http-options';
import { HttpService } from '../support/http.service';
import { SecurityService } from '../support/security.service';

@Injectable({
    providedIn: 'root'
})
export class OAuthService extends BaseService {

    constructor(private http: HttpService, private security: SecurityService) { super(); }

    requestToken(form: any, options: HttpOptions): void {
        const url = this.getBasePath() + '/oauth/token';
        options.headers = this.security.getBasicAuthorizationHeaders();
        const body = new FormData();
        for (const key in form) {
            if (Object.prototype.hasOwnProperty.call(form, key)) {
                body.append(key, form[key]);
            }
        }
        options.body = body;
        this.http.request('POST', url, options);
    }

}
