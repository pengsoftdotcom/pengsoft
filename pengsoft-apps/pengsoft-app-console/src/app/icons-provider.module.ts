import { NgModule } from '@angular/core';
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
    UserOutline
} from '@ant-design/icons-angular/icons';
import { NzIconModule, NZ_ICONS } from 'ng-zorro-antd/icon';


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
