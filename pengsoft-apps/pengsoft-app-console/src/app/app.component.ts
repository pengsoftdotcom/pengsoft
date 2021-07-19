import { Location } from '@angular/common';
import { Component, TemplateRef, ViewChild } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Route, Router } from '@angular/router';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzModalRef, NzModalService } from 'ng-zorro-antd/modal';
import { environment } from 'src/environments/environment';
import { ChangePasswordComponent } from './component/modal/change-password/change-password.component';
import { Field } from './component/support/form-item/field';
import { PersonComponent } from './pages/basedata/person/person.component';
import { DictionaryItemService } from './service/basedata/dictionary-item.service';
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

    @ViewChild('jobsTemplate', { static: true }) jobsTemplate: TemplateRef<any>;

    switchJobModal: NzModalRef;

    switchJobVisible = false;

    switchJobDisabled = false;

    @ViewChild('rolesTemplate', { static: true }) rolesTemplate: TemplateRef<any>;

    switchRoleModal: NzModalRef;

    switchRoleVisible = false;

    switchRoleDisabled = false;

    @ViewChild('organizationsTemplate', { static: true }) organizationsTemplate: TemplateRef<any>;

    switchOrganizationModal: NzModalRef;

    switchOrganizationVisible = false;

    switchOrganizationDisabled = false;

    @ViewChild('editPersonTemplate', { static: true }) editPersonTemplate: TemplateRef<any>;

    editPersonModal: NzModalRef;

    personFields: Array<Field> = [];

    errors: any = {};

    constructor(
        private dictionaryItem: DictionaryItemService,
        private title: Title,
        private router: Router,
        private location: Location,
        private security: SecurityService,
        private message: NzMessageService,
        private modal: NzModalService,
        private userDetailsService: UserDetailsService
    ) {
        if (security.isNotAuthenticated()) {
            this.signIn();
        }

        this.title.setTitle(environment.title);
        this.menus = this.router.config.filter(
            (route) => route.data || route.children
        );
        this.userDetails = this.security.userDetails;

        if (this.userDetails.jobs && this.userDetails.jobs.length > 0) {
            this.switchJobVisible = true;
            if (this.userDetails.jobs.length === 1) {
                this.switchJobDisabled = true;
            }
        }

        if (!this.userDetails.jobs && this.userDetails.primaryRole && this.userDetails.primaryRole.code !== 'org_admin') {
            this.switchRoleVisible = true;
            if (this.userDetails.roles.length === 1) {
                this.switchRoleDisabled = true;
            }
        }

        if (this.userDetails.primaryRole && this.userDetails.primaryRole.code === 'org_admin') {
            this.switchOrganizationVisible = true;
            if (this.userDetails.organizations.length === 1) {
                this.switchOrganizationDisabled = true;
            }
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
        PersonComponent.prototype.dictionaryItem = this.dictionaryItem;
        PersonComponent.prototype.initFields();
        this.personFields = PersonComponent.prototype.fields;
        this.editPersonModal = this.modal.create({
            nzTitle: '编辑基本信息',
            nzContent: this.editPersonTemplate,
            nzOnOk: () =>
                new Promise((resolve) => {
                    this.userDetailsService.savePerson(this.userDetails.person, {
                        errors: this.errors,
                        success: (res: any) => {
                            this.userDetails = res;
                            this.message.info('保存成功', { nzDuration: 1000 })
                                .onClose.subscribe(() => resolve(true));
                        },
                        failure: () => resolve(false)
                    });
                })
        });
    }

    switchRole(): void {
        this.switchRoleModal = this.modal.create({
            nzStyle: { top: '30%' },
            nzWidth: 450,
            nzTitle: '切换角色',
            nzContent: this.rolesTemplate,
            nzFooter: null,
        });
    }

    switchJob(): void {
        this.switchJobModal = this.modal.create({
            nzStyle: { top: '30%' },
            nzWidth: 450,
            nzTitle: '切换职位',
            nzContent: this.jobsTemplate,
            nzFooter: null,
        });
    }

    switchOrganization(): void {
        this.switchOrganizationModal = this.modal.create({
            nzStyle: { top: '30%' },
            nzWidth: 450,
            nzTitle: '切换机构',
            nzContent: this.organizationsTemplate,
            nzFooter: null,
        });
    }

    roleSwitched(): void {
        this.userDetailsService.setPrimaryRole(this.userDetails.primaryRole, {
            success: (res: any) => {
                this.userDetails = res;
                this.security.userDetails = res;
                this.message
                    .info('设置成功', { nzDuration: 1000 })
                    .onClose.subscribe(() => this.switchRoleModal.close());
            },
            failure: () => this.message.error('设置失败'),
        });
    }

    jobSwitched(): void {
        this.userDetailsService.setPrimaryJob(this.userDetails.primaryJob, {
            success: (res: any) => {
                this.userDetails = res;
                this.security.userDetails = res;
                this.message
                    .info('设置成功', { nzDuration: 1000 })
                    .onClose.subscribe(() => this.switchJobModal.close());
            },
            failure: () => this.message.error('设置失败'),
        });
    }

    organizationSwitched(): void {
        this.userDetailsService.setPrimaryOrganization(this.userDetails.primaryOrganization, {
            success: (res: any) => {
                this.userDetails = res;
                this.security.userDetails = res;
                this.message
                    .info('设置成功，页面即将刷新', { nzDuration: 1000 })
                    .onClose.subscribe(() => this.switchOrganizationModal.close());
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
                })
        });
    }

    signIn(): void {
        this.router.navigateByUrl('/sign-in');
    }

    signOut(): void {
        this.modal.confirm({
            nzTitle: '确定要退出登录吗？',
            nzOnOk: () => {
                this.userDetailsService.signOut();
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
            name = this.userDetails.person.name;
        }
        return name;
    }


}