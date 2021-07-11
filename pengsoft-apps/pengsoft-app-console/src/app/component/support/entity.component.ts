import { Injectable, OnInit } from '@angular/core';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzModalService } from 'ng-zorro-antd/modal';
import { NzTreeNodeOptions } from 'ng-zorro-antd/tree';
import { EntityService } from 'src/app/service/support/entity.service';
import { BaseComponent } from './base.component';
import { Button } from './button/button';
import { EditComponent } from './edit/edit.component';
import { FilterComponent } from './filter/filter.component';
import { Field } from './form-item/field';
import { ListComponent } from './list/list.component';
import { Page } from './list/page';

@Injectable()
export abstract class EntityComponent<S extends EntityService> extends BaseComponent implements OnInit {

    filterForm: any = {};

    filterWidth = 900;

    filterSpan = 12;

    editForm: any = {};

    editToolbar: Array<Button> = [];

    fields: Array<Field> = [];

    listData: Array<any> = [];

    pageData: Page = { page: 1, size: 20, total: 1, sort: [] };

    listToolbar: Array<Button> = [];

    listAction: Array<Button> = [];

    allowLoadNavData = false;

    navData: Array<NzTreeNodeOptions>;

    errors = {};

    constructor(
        public entity: S,
        public modal: NzModalService,
        public message: NzMessageService
    ) {
        super();
    }

    ngOnInit(): void {
        this.initFields();
        let fields = this.fields.filter(field => field.children);
        while (fields.length > 0) {
            const subFields = []
            fields.forEach(field => field.children.forEach(subField => {
                subFields.push(subField);
                if (field.parentCode) {
                    subField.parentCode = field.parentCode + '.' + field.code;
                } else {
                    subField.parentCode = field.code;
                }
                if (field.list.childrenVisible === false) {
                    subField.list.visible = false;
                    subField.list.childrenVisible = false;
                }
                if (field.edit.childrenVisible === false) {
                    subField.edit.visible = false;
                    subField.edit.childrenVisible = false;
                }
            }));
            fields = subFields.filter(subField => subField.children);
        }
        this.initEditToolbar();
        this.initListToolbar();
        this.initListAction();
        this.afterInit();
    }

    abstract initFields(): void;

    abstract getListComponent(): ListComponent;

    abstract getEditComponent(): EditComponent;

    initEditToolbar(): void {
        this.editToolbar = [
            { name: '保存', type: 'primary', size: 'default', action: () => this.save(), authority: this.getAuthority('save') }
        ];
    }

    initListToolbar(): void {
        this.listToolbar = [
            { name: '刷新', icon: 'reload', action: () => this.list(), authority: this.getAuthority('findPage') + ', ' + this.getAuthority('findAll') },
            { name: '新增', type: 'primary', action: () => this.edit(), authority: this.getAuthority('save') },
            { name: '批量删除', type: 'primary', danger: true, action: () => this.delete(), authority: this.getAuthority('delete') }
        ];
        if (this.fields.some(field => field.filter)
            || this.fields.filter(field => field.children).some(field => field.children.some(subField => subField.filter))) {
            this.listToolbar.splice(1, 0, {
                name: '搜索',
                icon: 'search',
                authority: this.getAuthority('findPage') + ', ' + this.getAuthority('findAll'),
                action: () => this.filter()
            });
        }
    }

    initListAction(): void {
        this.listAction = [
            { name: '查看', type: 'link', width: 30, action: (row: any) => this.edit(row), authority: this.getAuthority('findOne'), exclusive: this.getAuthority('save') },
            { name: '修改', type: 'link', width: 30, action: (row: any) => this.edit(row), authority: this.getAuthority('save') },
            { name: '删除', type: 'link', danger: true, width: 30, action: (row: any) => this.delete(row), authority: this.getAuthority('delete') }
        ];
    }

    protected getAuthority(action: string): string {
        if (action.indexOf('::') === -1) {
            const moduleCode = this.entity.modulePath.replace(/\//g, '_').replace(/-/g, '_');
            const entityCode = this.entity.entityPath.replace(/\//g, '_').replace(/-/g, '_');
            let actionCode = '';
            const length = action.length;
            for (let index = 0; index < length; index++) {
                const c = action.charAt(index);
                if (c === c.toUpperCase()) {
                    actionCode += '_' + c.toLowerCase();
                } else {
                    actionCode += c;
                }
            }
            return [moduleCode, entityCode, actionCode].join('::');
        } else {
            return action;
        }
    }

    afterInit(): void {
        if (this.allowLoadNavData) {
            this.loadNavData();
        } else {
            this.list();
        }
    }

    loadNavData(): void {
        // 实现加载导航数据
    }

    edit(row?: any): void {
        this.beforeEdit();
        const id = row ? row.id : null;
        this.entity.findOne(id, {
            before: () => this.getEditComponent().loading = true,
            success: (res: any) => {
                this.editForm = res;
                this.getEditComponent().show();
            },
            after: () => {
                this.getEditComponent().loading = false;
                this.afterEdit();
            }
        });
    }

    beforeEdit(): void {
        this.editForm = {};
        this.errors = {};
    }

    afterEdit(): void {
        // 空实现
    }

    save(): void {
        const form = this.buildForm();
        this.entity.save(form, {
            errors: this.errors,
            before: () => this.getEditComponent().loading = true,
            success: (res: any) => this.saveSuccess(res),
            after: () => this.getEditComponent().loading = false
        });
    }

    saveSuccess(res: any): void {
        this.message.info('保存成功');
        this.getEditComponent().hide();
        this.list();
    }

    protected buildForm() {
        const form = Object.assign({}, this.editForm);
        for (const key in form) {
            if (form.hasOwnProperty(key) && key.indexOf('.') > -1) {
                const code = key.split('.');
                if (!form[code[0]]) {
                    form[code[0]] = {};
                }
                form[code[0]][code[1]] = form[key];
                delete form[key];
            }
        }
        return form;
    }

    filter(): void {
        this.modal.create({
            nzBodyStyle: { padding: '16px', marginBottom: '-24px' },
            nzTitle: '搜索',
            nzWidth: this.filterWidth,
            nzContent: FilterComponent,
            nzComponentParams: {
                span: this.filterSpan,
                form: this.filterForm,
                fields: this.fields
            },
            nzOnOk: () => this.list(),
            nzCancelText: '重置',
            nzOnCancel: () => {
                this.filterForm = {};
                this.afterFilterFormReset();
            }
        });
    }

    afterFilterFormReset(): void {
        this.list();
    }

    list(): void {
        this.entity.findPage(this.filterForm, this.pageData, {
            before: () => this.getListComponent().loading = true,
            success: (res: any) => {
                this.getListComponent().allChecked = false;
                this.getListComponent().indeterminate = false;
                this.listData = res.content;
                this.pageData.total = res.totalElements;
            },
            after: () => this.getListComponent().loading = false
        });
    }

    delete(row?: any): void {
        const ids = row ? [row.id] : this.listData.filter(entity => entity.checked).map(entity => entity.id);
        if (ids.length > 0) {
            this.modal.confirm({
                nzTitle: '确定要删除这些数据吗？',
                nzOnOk: () => new Promise(resolve => {
                    this.entity.delete(ids, {
                        success: () => {
                            this.afterDelete(ids.map(id => this.listData.find(value => value.id === id)));
                            this.getListComponent().allChecked = false;
                            this.getListComponent().indeterminate = false;
                            this.message.info('删除成功');
                        },
                        after: resolve
                    });
                })
            });
        }
    }

    afterDelete(deletedRows: Array<any>): void {
        this.list();
    }

    sort(): void {
        const sortInfo = {};
        this.listData.filter(d => d.dirty).forEach(data => sortInfo[data.id] = data.sequence);
        if (Object.keys(sortInfo).length > 0) {
            this.entity.sort(sortInfo, {
                before: () => this.loading = true,
                success: () => {
                    this.message.info('排序成功');
                    this.list();
                },
                after: () => this.loading = false
            });
        }
    }

}
