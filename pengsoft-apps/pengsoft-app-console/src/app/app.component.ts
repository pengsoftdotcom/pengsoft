import { Location } from '@angular/common';
import { Component, TemplateRef, ViewChild } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Route, Router } from '@angular/router';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzModalRef, NzModalService } from 'ng-zorro-antd/modal';
import { environment } from 'src/environments/environment';
import { ChangePasswordComponent } from './component/modal/change-password/change-password.component';
import { UserDetailsService } from './service/security/user-details.service';
import { SecurityService } from './service/support/security.service';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent {

    isCollapsed = false;

    menus: any[] = [];

    userDetails: any = { user: {} };

    @ViewChild('jobs', { static: true }) public jobs: TemplateRef<any>;

    switchJobModal: NzModalRef;

    @ViewChild('roles', { static: true }) public roles: TemplateRef<any>;

    switchRoleModal: NzModalRef;

    @ViewChild('organizations', { static: true }) organizations: TemplateRef<any>;

    switchOrganizationModal: NzModalRef;

    constructor(
        private title: Title,
        private router: Router,
        private location: Location,
        private message: NzMessageService,
        private modal: NzModalService,
        private security: SecurityService,
        private userDetailsService: UserDetailsService
    ) {
        this.title.setTitle(environment.title);
        this.menus = this.router.config.filter(
            (route) => route.data || route.children
        );
        this.userDetails = this.security.userDetails;
        if (security.isNotAuthenticated()) {
            this.signIn();
        }
    }

    getMenuAuthority(route: Route): string {
        let authority: string;
        if (route.children) {
            authority = route.children
                .map((child) => child.data.code)
                .join(',');
        } else if (route.data) {
            authority = route.data.code;
        }
        return authority;
    }

    isMenuOpen(menu: any): boolean {
        const paths = this.location.path().split('/');
        paths.shift();
        return menu.path === paths.shift();
    }

    editPerson(): void {
        this.message.info('暂未开放，敬请期待');
    }

    switchRole(): void {
        this.switchRoleModal = this.modal.create({
            nzStyle: { top: '30%' },
            nzWidth: 450,
            nzTitle: '切换角色',
            nzContent: this.roles,
            nzFooter: null,
        });
    }

    switchJob(): void {
        this.switchJobModal = this.modal.create({
            nzStyle: { top: '30%' },
            nzWidth: 450,
            nzTitle: '切换职位',
            nzContent: this.jobs,
            nzFooter: null,
        });
    }

    switchOrganization(): void {
        this.switchJobModal = this.modal.create({
            nzStyle: { top: '30%' },
            nzWidth: 450,
            nzTitle: '切换机构',
            nzContent: this.organizations,
            nzFooter: null,
        });
    }

    primaryRoleChanged(): void {
        this.userDetailsService.setPrimaryRole(this.userDetails.primaryRole, {
            success: (res: any) => {
                console.log(res);
                this.security.userDetails = res;
                this.message
                    .info('设置成功', { nzDuration: 1000 })
                    .onClose.subscribe(() => this.switchRoleModal.close());
            },
            failure: () => this.message.error('设置失败'),
        });
    }

    primaryJobChanged(): void {
        this.userDetailsService.setPrimaryJob(this.userDetails.primaryJob, {
            success: (res: any) => {
                this.security.userDetails = res;
                this.message
                    .info('设置成功', { nzDuration: 1000 })
                    .onClose.subscribe(() => this.switchJobModal.close());
            },
            failure: () => this.message.error('设置失败'),
        });
    }

    primaryOrganizationChanged(): void {
        this.userDetailsService.setOrganization(this.userDetails.organization, {
            success: (res: any) => {
                this.security.userDetails = res;
                this.message
                    .info('设置成功，页面即将刷新', { nzDuration: 1000 })
                    .onClose.subscribe(() => window.location.reload());
            },
            failure: () => this.message.error('设置失败'),
        });
    }

    changePassword(): void {
        this.modal.create({
            nzContent: ChangePasswordComponent,
            nzStyle: { top: '30%' },
            nzWidth: 450,
            nzTitle: '修改密码',
            nzOnOk: (component) =>
                new Promise((resolve) => {
                    component.submit({
                        before: () => (component.loading = true),
                        success: () => resolve(true),
                        failure: () => resolve(false),
                        after: () => (component.loading = false),
                    });
                }),
        });
    }

    public signIn(): void {
        this.router.navigateByUrl('/sign-in');
    }

    public signOut(): void {
        this.modal.confirm({
            nzTitle: '确定要退出登录吗？',
            nzOnOk: () => {
                this.security.clear();
                this.signIn();
            },
        });
    }

    get avatar(): string {
        return (
            this.userDetails.person.avatar.accessPath + '?x-oss-process=image/resize,w_28,h_28'
        );
    }

    get name(): string {
        let name = this.userDetails.user.username;
        if (this.userDetails.person) {
            name = this.userDetails.person.nickname;
        }
        return name;
    }


}