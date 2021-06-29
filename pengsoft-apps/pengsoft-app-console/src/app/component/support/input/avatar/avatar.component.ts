import { Component } from '@angular/core';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzUploadChangeParam } from 'ng-zorro-antd/upload';
import { SecurityService } from 'src/app/service/support/security.service';
import { environment } from 'src/environments/environment';
import { InputComponent } from '../input.component';


@Component({
    selector: 'app-input-avatar',
    templateUrl: './avatar.component.html',
    styleUrls: ['./avatar.component.scss']
})
export class AvatarComponent extends InputComponent {

    constructor(private security: SecurityService, private message: NzMessageService) { super(); }

    get path(): string {
        return environment.gateway.path + '/api/asset/upload';
    }

    get headers(): any {
        return this.security.getBearerAuthorizationHeaders();
    }

    get thumbnail(): string {
        return this.form[this.edit.code].accessPath + '?x-oss-process=image/resize,w_' + this.edit.input.width + ',h_' + this.edit.input.height;
    }

    modelChange(event: NzUploadChangeParam) {
        switch (event.file.status) {
            case 'uploading':
                this.loading = true;
                break;
            case 'done':
                this.loading = false;
                const res = event.file.response;
                if (res && res.length > 0) {
                    this.form[this.edit.code] = res[0];
                }
                break;
            case 'error':
                this.message.error('上传失败：' + event.file.error.error.error_description);
                this.loading = false;
                break;
        }
    }

}
