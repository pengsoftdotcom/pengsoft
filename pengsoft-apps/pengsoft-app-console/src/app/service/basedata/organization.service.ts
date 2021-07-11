import { Injectable } from '@angular/core';
import { EntityService } from '../support/entity.service';
import { HttpOptions } from '../support/http-options';
import { HttpService } from '../support/http.service';

@Injectable({
    providedIn: 'root'
})
export class OrganizationService extends EntityService {

    constructor(protected http: HttpService) { super(http); }

    get modulePath(): string {
        return 'basedata';
    }

    get entityPath(): string {
        return 'organization';
    }

    findPageOfAvailableConsumers(supplier: any, options: HttpOptions): void {
        const url = this.getApiPath('find-page-of-available-consumers');
        options.params = { 'supplier.id': supplier.id };
        this.http.request('GET', url, options);
    }

    findAllAvailableConsumers(supplier: any, options: HttpOptions): void {
        const url = this.getApiPath('find-all-available-consumers');
        options.params = { 'supplier.id': supplier.id };
        this.http.request('GET', url, options);
    }

    findPageOfAvailableSuppliers(consumner: any, options: HttpOptions): void {
        const url = this.getApiPath('find-page-of-available-suppliers');
        options.params = { 'consumer.id': consumner.id };
        this.http.request('GET', url, options);
    }

    findAllAvailableSuppliers(consumner: any, options: HttpOptions): void {
        const url = this.getApiPath('find-all-available-suppliers');
        options.params = { 'consumer.id': consumner.id };
        this.http.request('GET', url, options);
    }

}
