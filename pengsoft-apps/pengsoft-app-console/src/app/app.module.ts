import { registerLocaleData } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import zh from '@angular/common/locales/zh';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NzAvatarModule } from 'ng-zorro-antd/avatar';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzDividerModule } from 'ng-zorro-antd/divider';
import { NzDropDownModule } from 'ng-zorro-antd/dropdown';
import { NzGridModule } from 'ng-zorro-antd/grid';
import { NZ_I18N, zh_CN } from 'ng-zorro-antd/i18n';
import { NzLayoutModule } from 'ng-zorro-antd/layout';
import { NzMenuModule } from 'ng-zorro-antd/menu';
import { NzModalModule } from 'ng-zorro-antd/modal';
import { NzRadioModule } from 'ng-zorro-antd/radio';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ComponentModule } from './component/component.module';
import { DirectiveModule } from './directive/directive.module';
import { IconsProviderModule } from './icons-provider.module';
import { BasedataModule } from './pages/basedata/basedata.module';
import { SecurityModule } from './pages/security/security.module';
import { DashboardComponent } from './pages/shared/dashboard/dashboard.component';
import { FullScreenForgetPasswordComponent } from './pages/shared/forget-password/forget-password.component';
import { FullScreenSignInComponent } from './pages/shared/sign-in/sign-in.component';
import { SystemModule } from './pages/system/system.module';


registerLocaleData(zh);

@NgModule({
    declarations: [
        AppComponent,
        FullScreenSignInComponent,
        DashboardComponent,
        FullScreenForgetPasswordComponent
    ],
    imports: [
        HttpClientModule,
        FormsModule,
        BrowserModule,
        BrowserAnimationsModule,
        AppRoutingModule,
        NzAvatarModule,
        NzButtonModule,
        NzDropDownModule,
        NzDividerModule,
        NzGridModule,
        NzLayoutModule,
        NzMenuModule,
        NzModalModule,
        NzRadioModule,
        ComponentModule,
        DirectiveModule,
        IconsProviderModule,
        BasedataModule,
        SystemModule,
        SecurityModule
    ],
    providers: [{ provide: NZ_I18N, useValue: zh_CN }],
    bootstrap: [AppComponent]
})
export class AppModule { }
