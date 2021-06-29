import { HttpHeaders, HttpParams } from '@angular/common/http';

export interface HttpOptions {
    body?: any;
    errors?: any;
    headers?: HttpHeaders | { [header: string]: string | Array<string> };
    observe?: 'body';
    params?: HttpParams | { [param: string]: string | Array<string> };
    reportProgress?: boolean;
    responseType?: 'arraybuffer' | 'blob' | 'json' | 'text';
    withCredentials?: boolean;
    before?: () => void;
    success?: (res: any) => void;
    failure?: (err: any) => void;
    after?: () => void;
}