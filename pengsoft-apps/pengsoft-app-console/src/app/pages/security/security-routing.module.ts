import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthorityComponent } from './authority/authority.component';
import { RoleComponent } from './role/role.component';
import { UserComponent } from './user/user.component';


const routes: Routes = [
    {
        path: 'security',
        data: { name: '权限设置', icon: 'safety' },
        children: [
            { path: 'user', component: UserComponent, data: { code: 'security::user::find_page', name: '用户' } },
            { path: 'role', component: RoleComponent, data: { code: 'security::role::find_page', name: '角色' } },
            { path: 'authority', component: AuthorityComponent, data: { code: 'security::authority::find_page', name: '权限' } }
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class SecurityRoutingModule { }
