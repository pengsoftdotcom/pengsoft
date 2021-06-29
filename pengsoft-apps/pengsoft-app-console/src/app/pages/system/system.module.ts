import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ComponentModule } from 'src/app/component/component.module';
import { AssetComponent } from './asset/asset.component';
import { CaptchaComponent } from './captcha/captcha.component';
import { DictionaryItemComponent } from './dictionary-item/dictionary-item.component';
import { DictionaryTypeComponent } from './dictionary-type/dictionary-type.component';
import { RegionComponent } from './region/region.component';
import { SystemRoutingModule } from './system-routing.module';



@NgModule({
    declarations: [
        AssetComponent,
        CaptchaComponent,
        DictionaryTypeComponent,
        DictionaryItemComponent,
        RegionComponent
    ],
    imports: [
        CommonModule,
        FormsModule,
        SystemRoutingModule,
        ComponentModule
    ]
})
export class SystemModule { }
