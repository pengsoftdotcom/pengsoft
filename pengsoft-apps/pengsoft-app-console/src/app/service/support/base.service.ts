import { environment } from 'src/environments/environment';
import { HttpOptions } from './http-options';

export class BaseService {

    getBasePath(): string {
        return environment.gateway.path;
    }

    getApiPath(action: string): string {
        return this.getBasePath() + '/api' + action;
    }

    protected mergeSuccess(success: (res: any) => void, options: HttpOptions): HttpOptions {
        options = options ? options : {};
        const original = options.success;
        if (success) {
            options.success = (res: any) => {
                success(res);
                original(res);
            };
        } else {
            options.success = (res: any) => original(res);
        }
        return options;
    }

    protected mergeFailure(failure: (res: any) => void, options: HttpOptions): HttpOptions {
        options = options ? options : {};
        const original = options.failure;
        if (failure) {
            options.failure = (err: any) => {
                failure(err);
                original(err);
            };
        } else {
            options.failure = (err: any) => original(err);
        }
        return options;
    }

    protected getStringValue(value: string): string {
        return value ? value : '';
    }

    protected clearErrors(errors: any): void {
        for (const key in errors) {
            if (errors.hasOwnProperty(key)) {
                delete errors[key];
            }
        }
    }

}
