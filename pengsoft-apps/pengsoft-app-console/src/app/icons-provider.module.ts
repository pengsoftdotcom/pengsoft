import { NgModule } from '@angular/core';
import { NZ_ICONS, NzIconModule } from 'ng-zorro-antd/icon';

import {
    BookOutline,
    DashboardOutline,
    FormOutline,
    HomeOutline,
    LockOutline,
    MenuFoldOutline,
    MenuUnfoldOutline,
    PlusOutline,
    ReloadOutline,
    SafetyOutline,
    SettingOutline,
    UserOutline,
} from '@ant-design/icons-angular/icons';

const icons = [
    BookOutline,
    DashboardOutline,
    FormOutline,
    HomeOutline,
    LockOutline,
    MenuFoldOutline,
    MenuUnfoldOutline,
    PlusOutline,
    ReloadOutline,
    SafetyOutline,
    SettingOutline,
    UserOutline
];

@NgModule({
    imports: [NzIconModule],
    exports: [NzIconModule],
    providers: [
        { provide: NZ_ICONS, useValue: icons }
    ]
})
export class IconsProviderModule {
}
