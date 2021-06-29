import { Component } from '@angular/core';
import { OrganizationService } from 'src/app/service/basedata/organization.service';
import { EntityUtils } from 'src/app/util/entity-utils';
import { FieldUtils } from 'src/app/util/field-utils';
import { BaseComponent } from '../../support/base.component';
import { Option } from '../../support/form-item/option';
import { InputComponent } from '../../support/input/input.component';

@Component({
    selector: 'app-switch-organization',
    templateUrl: './switch-organization.component.html',
    styleUrls: ['./switch-organization.component.scss']
})
export class SwitchOrganizationComponent extends BaseComponent {

    field = FieldUtils.buildTreeSelect({
        code: 'organization',
        edit: {
            label: { visible: false },
            input: {
                load: (component: InputComponent, event?: string) => {
                    const params: any = {};
                    if (event) {
                        params.name = event;
                    }
                    this.organization.findAll(params, {
                        before: () => this.loading = true,
                        success: (res: any) => component.edit.input.options = EntityUtils.convertListToTree(res) as Option[],
                        after: () => this.loading = false
                    });
                }
            }
        }
    });

    form: any = {};

    constructor(private organization: OrganizationService) { super(); }

}
