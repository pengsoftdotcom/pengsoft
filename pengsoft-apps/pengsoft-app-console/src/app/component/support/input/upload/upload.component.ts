import { Component, OnInit } from '@angular/core';
import { NzUploadFile } from 'ng-zorro-antd/upload';
import { Observable } from 'rxjs';
import { AssetService } from 'src/app/service/basedata/asset.service';
import { SecurityService } from 'src/app/service/support/security.service';
import { environment } from 'src/environments/environment';
import { InputComponent } from '../input.component';

@Component({
    selector: 'app-input-upload',
    templateUrl: './upload.component.html',
    styleUrls: ['./upload.component.scss']
})
export class UploadComponent extends InputComponent implements OnInit {

    files = [];

    constructor(private security: SecurityService, private asset: AssetService) { super(); }

    get path(): string {
        return environment.gateway.path + '/api/asset/upload';
    }

    get headers(): any {
        return this.security.getBearerAuthorizationHeaders();
    }

    ngOnInit(): void { }

    download(file: NzUploadFile): void {
        const asset = file.response[0];
        window.open(asset.accessPath);
    }

    remove(file: NzUploadFile): boolean | Observable<boolean> {
        return new Observable(observer => this.asset.delete([file.response[0].id], {
            success: () => observer.next(true),
            failure: () => observer.next(false)
        }));
    }

}
