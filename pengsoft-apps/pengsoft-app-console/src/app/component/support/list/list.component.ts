import { Component, EventEmitter, Injectable, Input, OnInit, Output, ViewChild } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { NzTreeNodeOptions } from 'ng-zorro-antd/tree';
import { SecurityService } from 'src/app/service/support/security.service';
import { BaseComponent } from '../base.component';
import { Button } from '../button/button';
import { Field } from '../form-item/field';
import { Page } from './page';
import { Sort } from './sort';

@Component({
    selector: 'app-list',
    templateUrl: './list.component.html',
    styleUrls: ['./list.component.scss']
})
export class ListComponent extends BaseComponent implements OnInit {

    @Input() title = '';

    @Input() fields: Array<Field> = [];

    visibleFields = [];

    @Input() navData: Array<NzTreeNodeOptions>;

    @Input() listData = [];

    @Input() pageData: Page;

    @Input() toolbar: Array<Button> = [];

    @Input() action: Array<Button> = [];

    @Output() pageChange = new EventEmitter<Page>();

    @Output() expandChange = new EventEmitter<any>();

    @Input() sortable = false;

    @Input() footerToolbar = false;

    @Output() sequenceChange = new EventEmitter<any>();

    @Output() nav = new EventEmitter<any>();

    @ViewChild('table', { static: true }) table: any;

    tableNavHeight: any = 0;

    tableBodyHeight: any = 0;

    tableWidthConfig = [];

    allChecked = false;

    indeterminate = false;

    checkedCount = 0;

    pageable = true;

    treeable = false;

    groupable = false;

    firstVisibleFieldIndex = -1;

    // isWindows = false;

    constructor(private security: SecurityService, public sanitizer: DomSanitizer) {
        super();
        this.title = '列表';
    }

    ngOnInit(): void {
        // this.isWindows = window.navigator.userAgent.indexOf('Windows') > -1;
        this.initVisibleFields();
        this.initGroupable();
        this.initTreeable();
        this.initPageable();
        this.initTableWidthConfig();
        this.initTableBodyHeight();
    }

    private initVisibleFields() {
        this.fields.filter(field => field.list.visible).forEach(field => {
            if (field.children) {
                field.children.filter(subfield => subfield.list.visible).forEach(subfield => this.visibleFields.push(subfield));
            } else {
                this.visibleFields.push(field);
            }
        });
    }

    private initGroupable() {
        this.groupable = this.fields.some(field => field.children);
    }

    private initTreeable() {
        this.treeable = this.fields.some(field => field.code === 'parent');
    }

    private initPageable() {
        if (this.pageData === undefined) {
            this.pageable = false;
            this.pageData = { page: 1, size: 20 };
        }
    }

    private initTableBodyHeight() {
        const totalHeight = this.table.elementRef.nativeElement.parentNode.parentNode.parentNode.offsetHeight - 46;
        this.tableNavHeight = totalHeight - 24;

        const listTitleHeight = 41;
        const toolbarButtonsHeight = 57;
        const tableHeaderHeight = this.groupable ? 47 * 2 : 47;

        this.tableBodyHeight = totalHeight;
        this.tableBodyHeight -= listTitleHeight;
        this.tableBodyHeight -= toolbarButtonsHeight;
        this.tableBodyHeight -= tableHeaderHeight;
        this.tableBodyHeight += 'px';
    }

    private initTableWidthConfig() {
        const checkAllWidth = 46;
        const sortWidth = 82;
        let actionWidth = 17;
        this.action = this.action.filter(button => !button.authority || this.security.hasAnyAuthority(button.authority));
        if (this.action.length === 1 && this.action[0].divider) {
            this.action[0].divider = false;
            this.action[0].width = this.action[0].width - 17;
        }
        this.action.forEach(button => actionWidth += button.width);
        this.tableWidthConfig.push(checkAllWidth);
        this.fields.forEach(field => {
            if (field.children) {
                field.children.forEach(subfield => this.fillWidthConfig(subfield));
            }
            else {
                this.fillWidthConfig(field);
            }
        });
        if (this.sortable) {
            this.tableWidthConfig.push(sortWidth);
        }
        this.tableWidthConfig.push(actionWidth);
        this.tableWidthConfig = this.tableWidthConfig.map(width => width ? width + 'px' : null);
    }

    private fillWidthConfig(field: Field) {
        if (field.list.visible) {
            if (field.list.width) {
                this.tableWidthConfig.push(field.list.width);
            } else {
                this.tableWidthConfig.push(null);
            }
        }
    }

    private initLefWidthAndCount(field: Field, leftWidth: any, noWidthColumbCount: number) {
        if (field.list.visible) {
            if (field.list.width) {
                leftWidth -= field.list.width;
            } else {
                noWidthColumbCount++;
            }
        }
        return { leftWidth, noWidthColumbCount };
    }

    checkAll(allChecked: boolean): void {
        this.indeterminate = false;
        this.allChecked = allChecked;
        this.checkedCount = allChecked ? this.listData.length : 0;
        this.listData.forEach(data => data.checked = allChecked);
    }

    check(): void {
        this.checkedCount = this.listData.filter(d => d.checked).length;
        this.allChecked = this.listData.every(d => d.checked);
        this.indeterminate = !this.allChecked && !this.listData.every(d => !d.checked);
    }

    isRowVisible(row: any): boolean {
        if (this.treeable) {
            const parents = this.listData.filter((data, i) => row.id !== data.id && row.parentIds.indexOf(data.id) > -1);
            if (parents.length > 0) {
                return parents.every(parent => parent.expand);
            }
        }
        return true;
    }

    getRowspan(field?: Field): number {
        if (this.groupable && (!field || !field.children)) {
            return 2;
        } else {
            return 1;
        }
    }

    getColspan(field: Field): number {
        if (field.children) {
            return field.children.filter(subfield => subfield.list.visible).length;
        } else {
            return 1;
        }
    }

    render(field: Field, row: any): any {
        const list = field.list;
        let value: any;
        if (field.parentCode) {
            value = row[field.parentCode];
        } else {
            value = row;
        }
        if (list.render) {
            value = list.render(field, value, this.sanitizer);
        } else {
            value = value ? value[list.code] : null;
        }
        if (value === undefined || value === null) {
            return '-';
        } else {
            return value;
        }
    }

    sortChange(field: Field, direction: string): void {
        const sort: Sort = { code: field.code, direction: null, priority: field.list.sortPriority };
        switch (direction) {
            case 'ascend':
                sort.direction = 'asc';
                break;
            case 'descend':
                sort.direction = 'desc';
                break;
            default:
                sort.direction = null;
                break;
        }
        const index = this.pageData.sort.findIndex(value => value.code === field.code);
        if (index > -1) {
            this.pageData.sort.splice(index, 1);
        }
        if (sort.direction) {
            this.pageData.sort.push(sort);
        }
        this.pageData.sort.sort((a, b) => {
            if (a.priority < b.priority) {
                return -1;
            } else if (a.priority === b.priority) {
                return 0;
            } else {
                return 1;
            }
        });
        this.pageChange.emit();
    }

    isButtonDisabled(button: Button, row?: any): boolean {
        if (button.disabled) {
            return button.disabled(row);
        }
        return false;
    }

}
