import { EntityService } from './entity.service';
import { HttpOptions } from './http-options';
import { HttpService } from './http.service';

export abstract class TreeEntityService extends EntityService {

    constructor(protected http: HttpService) { super(http); }


    findAllByParent(parent: any, params: any, options: HttpOptions): void {
        const url = this.getApiPath('find-all-by-parent');
        options.params = Object.assign({}, params);
        if (parent && parent.id) {
            options.params['parent.id'] = parent.id;
        }
        this.http.request('GET', url, options);
    }

    findAllExcludeSelfAndItsChildrenByParent(parent: any, self: any, params: any, options: HttpOptions): void {
        const url = this.getApiPath('find-all-exclude-self-and-its-children-by-parent');
        options.params = Object.assign({}, params);
        if (parent && parent.id) {
            options.params['parent.id'] = parent.id;
        }
        if (self && self.id) {
            options.params['self.id'] = self.id;
        }
        this.http.request('GET', url, options);
    }

    findAllExcludeSelfAndItsChildren(self: any, params: any, options: HttpOptions): void {
        const url = this.getApiPath('find-all-exclude-self-and-its-children');
        options.params = Object.assign({}, params);
        if (self && self.id) {
            options.params['self.id'] = self.id;
        }
        this.http.request('GET', url, options);
    }

}
