import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AssetComponent } from './asset/asset.component';
import { CaptchaComponent } from './captcha/captcha.component';
import { DictionaryTypeComponent } from './dictionary-type/dictionary-type.component';
import { RegionComponent } from './region/region.component';


const routes: Routes = [
    {
        path: 'system',
        data: { name: '系统设置', icon: 'setting' },
        children: [
            { path: 'region', component: RegionComponent, data: { code: 'system::region::find_all_by_parent', name: '行政区划' } },
            { path: 'asset', component: AssetComponent, data: { code: 'system::asset::find_page', name: '附件' } },
            { path: 'dictionary-type', component: DictionaryTypeComponent, data: { code: 'system::dictionary_type::find_page', name: '数据字典' } },
            { path: 'captcha', component: CaptchaComponent, data: { code: 'system::captcha::find_page', name: '验证码' } },
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class SystemRoutingModule { }
