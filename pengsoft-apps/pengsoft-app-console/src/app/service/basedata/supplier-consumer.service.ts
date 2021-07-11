import { Injectable } from '@angular/core';
import { EntityService } from '../support/entity.service';
import { HttpOptions } from '../support/http-options';
import { HttpService } from '../support/http.service';

@Injectable({
    providedIn: 'root'
})
export class SupplierConsumerService extends EntityService {

    constructor(protected http: HttpService) { super(http); }

    get modulePath(): string {
        return 'basedata';
    }

    get entityPath(): string {
        return 'supplier-consumer';
    }

    saveSupplier(form: any, options: HttpOptions): void {
        const url = this.getApiPath('save-supplier');
        options.body = Object.assign({}, form);
        this.http.request('POST', url, options);
    }

    saveConsumer(form: any, options: HttpOptions): void {
        const url = this.getApiPath('save-consumer');
        options.body = Object.assign({}, form);
        this.http.request('POST', url, options);
    }

}
