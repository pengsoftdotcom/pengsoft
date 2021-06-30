import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { StoreService } from './store.service';

@Injectable({
    providedIn: 'root'
})
export class SecurityService {

    private readonly ACCESS_TOKEN = 'accessToken';

    private readonly USER_DETAILS = 'userDetails';

    constructor(private store: StoreService) { }

    getBearerAuthorizationHeaders(): any {
        return { Authorization: 'Bearer ' + this.accessToken.value };
    }

    getBasicAuthorizationHeaders(): any {
        return { Authorization: environment.authorization.basic };
    }

    isAuthenticated(): boolean {
        const accessToken = this.accessToken;
        return accessToken.expiredAt && accessToken.expiredAt - new Date().getTime() + 1000 * 60 * 30 > 0;
    }

    isNotAuthenticated(): boolean {
        return !this.isAuthenticated();
    }

    clear(): void {
        this.store.clear();
    }

    get accessToken(): any {
        return Object.assign({}, this.store.get(this.ACCESS_TOKEN));
    }

    set accessToken(accessToken: any) {
        this.store.set(this.ACCESS_TOKEN, accessToken);
    }

    get userDetails(): any {
        return Object.assign({ user: {} }, this.store.get(this.USER_DETAILS));
    }

    set userDetails(userDetails: any) {
        this.store.set(this.USER_DETAILS, userDetails);
    }

    get urlsPermitted(): Array<string> {
        return ['/oauth/token'];
    }

    hasAnyAuthority(authorityString: string, exclusive?: string): boolean {
        const authorities = [];
        if (authorityString) {
            if (authorityString.indexOf(',') === -1) {
                authorities.push(authorityString);
            } else {
                authorityString.split(',')
                    .map(authority => authority.trim())
                    .forEach(authority => authorities.push(authority));
            }
        }
        const userAuthorities: Array<string> = this.userDetails && this.userDetails.authorities ? this.userDetails.authorities : [];
        if (authorities.length > 0) {
            if (authorities.some(authority => userAuthorities.includes(authority))) {
                if (exclusive && userAuthorities.includes(exclusive)) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

}
