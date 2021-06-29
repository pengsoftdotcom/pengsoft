import { Injectable } from '@angular/core';
import { TreeEntityService } from '../support/tree-entity.service';
import { HttpService } from '../support/http.service';

@Injectable({
    providedIn: 'root'
})
export class PostService extends TreeEntityService {

    constructor(protected http: HttpService) { super(http); }

    get modulePath(): string {
        return 'basedata';
    }

    get entityPath(): string {
        return 'post';
    }

}
