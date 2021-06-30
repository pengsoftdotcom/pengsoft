import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
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

    @Output() sequenceChange = new EventEmitter<any>();

    @Output() nav = new EventEmitter<any>();

    @ViewChild('table', { static: true }) table: any;

    navHeight: any = 0;

    bodyHeight: any = 0;

    widthConfig = [];

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
        this.initWidthConfig();
        this.initBodyHeight();
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

    private initBodyHeight() {
        const totalHeight = this.table.elementRef.nativeElement.parentNode.parentNode.parentNode.offsetHeight - 64;
        this.navHeight = totalHeight - 24;

        const titleHeight = 41;
        const toolbarHeight = 57;
        const headerHeight = this.groupable ? 47 * 2 : 47;

        this.bodyHeight = totalHeight;
        this.bodyHeight -= titleHeight;
        this.bodyHeight -= toolbarHeight;
        this.bodyHeight -= headerHeight;
        this.bodyHeight += 'px';
    }

    private initWidthConfig() {
        const checkAllWidth = 46;
        this.widthConfig.push(checkAllWidth);

        const sortWidth = 82;
        if (this.sortable) {
            this.widthConfig.push(sortWidth);
        }

        this.fields.forEach(field => {
            if (field.children) {
                field.children.forEach(subfield => this.fillWidthConfig(subfield));
            } else {
                this.fillWidthConfig(field);
            }
        });

        let actionWidth = 17;
        this.action = this.action.filter(button => this.security.hasAnyAuthority(button.authority, button.exclusive));
        this.action.forEach((button, index) => {
            actionWidth += button.width;
            if (index + 1 < this.action.length) {
                button.divider = true;
                actionWidth += 17;
            } else {
                button.divider = false;
            }
        });
        if (this.action.length > 0) {
            this.widthConfig.push(actionWidth);
        }

        this.widthConfig = this.widthConfig.map(width => width ? width + 'px' : null);
    }

    private fillWidthConfig(field: Field) {
        if (field.list.visible) {
            if (field.list.width) {
                this.widthConfig.push(field.list.width);
            } else {
                this.widthConfig.push(null);
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
