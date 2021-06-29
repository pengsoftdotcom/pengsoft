import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './pages/shared/dashboard/dashboard.component';
import { FullScreenForgetPasswordComponent } from './pages/shared/forget-password/forget-password.component';
import { FullScreenSignInComponent } from './pages/shared/sign-in/sign-in.component';

const routes: Routes = [
    { path: '', pathMatch: 'full', redirectTo: 'dashboard' },
    { path: 'dashboard', component: DashboardComponent, data: { name: '首页', icon: 'home' } },
    { path: 'sign-in', component: FullScreenSignInComponent },
    { path: 'forget-password', component: FullScreenForgetPasswordComponent }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }
