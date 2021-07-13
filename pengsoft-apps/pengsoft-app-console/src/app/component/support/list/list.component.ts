import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { deepCopy } from 'deep-copy-ts';
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

    @Input() checkbox = true;

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

    depth = 0;

    fieldsArray = [];

    firstVisibleFieldIndex = -1;

    constructor(private security: SecurityService, public sanitizer: DomSanitizer) {
        super();
        this.title = '列表';
    }

    ngOnInit(): void {
        this.initVisibleFields();
        this.initGroupable();
        this.initTreeable();
        this.initPageable();
        this.initWidthConfig();
        this.initBodyHeight();
    }

    private initVisibleFields() {
        let fields = deepCopy<Array<Field>>(this.fields);
        while (fields.length > 0) {
            this.depth++;
            if (fields.filter(field => field.list.visible).length > 0) {
                this.fieldsArray.push(fields);
            }
            let subFields = [];
            fields.filter(field => field.children).forEach(field => field.children.forEach(subField => subFields.push(subField)));
            fields = subFields;
        }

        const queue = [];
        this.fields.forEach(field => {
            queue.push(field);
            while (queue.length > 0) {
                field = queue.shift();
                if (field.children) {
                    field.children.forEach((subField: any) => queue.push(subField));
                } else if (field.list.visible) {
                    this.visibleFields.push(field);
                }
            }
        });
    }

    private initGroupable() {
        this.groupable = this.fields.some(field => field.list.childrenVisible && field.children);
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
        if (this.checkbox) {
            const checkAllWidth = 46;
            this.widthConfig.push(checkAllWidth);
        }

        const sortWidth = 82;
        if (this.sortable) {
            this.widthConfig.push(sortWidth);
        }

        this.visibleFields.forEach(field => {
            if (field.children) {
                field.children.forEach((subField: Field) => this.fillWidthConfig(subField));
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

    isVisible(row: any): boolean {
        if (this.treeable) {
            const parents = this.listData.filter((data, i) => row.id !== data.id && row.parentIds.indexOf(data.id) > -1);
            if (parents.length > 0) {
                return parents.every(parent => parent.expand);
            }
        }
        return true;
    }

    getRowspan(field?: Field, index?: number): number {
        let rowspan = 1;
        if (!field) {
            rowspan = this.fieldsArray.length;
        } else if (!field.children) {
            rowspan = this.fieldsArray.length - index;
        }
        return rowspan;
    }

    getColspan(field: Field): number {
        if (field.children) {
            let length = 0;
            const queue = [field];
            while (queue.length > 0) {
                const f = queue.shift();
                if (f.children) {
                    f.children.forEach(subField => queue.push(subField))
                } else if (f.list.visible) {
                    length++;
                }
            }
            return length;
        } else {
            return 1;
        }
    }

    render(field: Field, row: any): any {
        const list = field.list;
        let value: any;
        value = row;
        if (field.parentCode) {
            const codes = field.parentCode.split('.');
            for (const code of codes) {
                value = value[code];
            }
        }
        if (list.render) {
            value = list.render(field, value, this.sanitizer);
        } else {
            value = value !== undefined && value !== null ? value[list.code] : null;
        }
        if (value === undefined || value === null) {
            return '-';
        } else {
            return value;
        }
    }

    sortChange(field: Field, direction: string): void {
        const code = field.parentCode ? field.parentCode + '.' + field.code : field.code;
        const sort: Sort = { code, direction: null, priority: field.list.sortPriority };
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
        const index = this.pageData.sort.findIndex(value => value.code === code);
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

    isDisabled(button: Button, row?: any): boolean {
        if (button.disabled) {
            return button.disabled(row);
        }
        return false;
    }

    counter(i: number) {
        return new Array(i);
    }

}
