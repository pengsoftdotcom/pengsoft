import { Injectable } from '@angular/core';
import { HttpOptions } from '../support/http-options';
import { HttpService } from '../support/http.service';
import { TreeEntityService } from '../support/tree-entity.service';

@Injectable({
    providedIn: 'root'
})
export class DictionaryItemService extends TreeEntityService {

    constructor(protected http: HttpService) { super(http); }

    get modulePath(): string {
        return 'system';
    }

    get entityPath(): string {
        return 'dictionary-item';
    }

    findAllByTypeCode(code: string, params: any, options: HttpOptions): void {
        const url = this.getApiPath('find-all-by-type-code');
        options.params = Object.assign({ code }, params);
        this.http.request('GET', url, options);
    }

}
