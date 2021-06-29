import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ComponentModule } from 'src/app/component/component.module';
import { AuthorityComponent } from './authority/authority.component';
import { RoleComponent } from './role/role.component';
import { SecurityRoutingModule } from './security-routing.module';
import { UserComponent } from './user/user.component';



@NgModule({
    declarations: [UserComponent, RoleComponent, AuthorityComponent],
    imports: [
        CommonModule,
        SecurityRoutingModule,
        ComponentModule
    ]
})
export class SecurityModule { }
