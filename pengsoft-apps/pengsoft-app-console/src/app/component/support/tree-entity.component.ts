import { Injectable } from '@angular/core';
import { NzFormatEmitEvent } from 'ng-zorro-antd/tree';
import { TreeEntityService } from 'src/app/service/support/tree-entity.service';
import { EntityUtils } from 'src/app/util/entity-utils';
import { FieldUtils } from 'src/app/util/field-utils';
import { EntityComponent } from './entity.component';
import { Option } from './form-item/option';
import { InputComponent } from './input/input.component';

@Injectable()
export abstract class TreeEntityComponent<S extends TreeEntityService> extends EntityComponent<S> {

    get parentQueryLazy(): boolean {
        return false;
    }

    get parentQueryParams(): any {
        return null;
    }

    initFields(): void {
        this.fields = [
            FieldUtils.buildTreeSelect({
                code: 'parent', name: '上级',
                list: { visible: false },
                edit: {
                    input: {
                        lazy: this.parentQueryLazy,
                        load: (component: InputComponent, event?: NzFormatEmitEvent) => {
                            const self = this.editForm;
                            if (this.parentQueryLazy) {
                                const parent = event ? event.node.origin.value : null;
                                this.entity.findAllExcludeSelfAndItsChildrenByParent(parent, self, this.parentQueryParams, {
                                    before: () => component.loading = true,
                                    success: (res: any) => {
                                        if (event) {
                                            event.node.addChildren(EntityUtils.convertListToTree(res));
                                        } else {
                                            component.edit.input.options = EntityUtils.convertListToTree(res) as Option[];
                                        }
                                    },
                                    after: () => component.loading = false
                                });
                            } else {
                                this.entity.findAllExcludeSelfAndItsChildren(self, this.parentQueryParams, {
                                    before: () => component.loading = true,
                                    success: (res: any) =>
                                        component.edit.input.options = EntityUtils.convertListToTree(res) as Option[],
                                    after: () => component.loading = false
                                });
                            }
                        }
                    }
                }
            })
        ];
    }

    list(): void {
        if (this.parentQueryLazy) {
            this.entity.findAllByParent(null, this.filterForm, {
                before: () => this.getListComponent().loading = true,
                success: (res: any) => {
                    const tree = EntityUtils.convertListToTree(res);
                    const list = EntityUtils.convertTreeToList(tree, node => {
                        const value = node.value;
                        value.expand = false;
                        value.loaded = false;
                        value.children = node.children;
                        return value;
                    });
                    this.listData = list;
                },
                after: () => this.getListComponent().loading = false
            });
        } else {
            this.entity.findAll(this.filterForm, {
                before: () => this.getListComponent().loading = true,
                success: (res: any) => {
                    const tree = EntityUtils.convertListToTree(res);
                    const list = EntityUtils.convertTreeToList(tree, node => {
                        const value = node.value;
                        value.expand = true;
                        value.loaded = true;
                        value.children = node.children;
                        const parentIds = value.parentIds ? value.parentIds + '::' + value.id : value.id;
                        value.leaf = !res.some(data => data.parentIds.startsWith(parentIds));
                        return value;
                    });
                    this.listData = list;
                },
                after: () => this.getListComponent().loading = false
            });
        }
    }

    afterDelete(deletedRows: Array<any>): void {
        deletedRows.forEach(row => {
            const i = this.listData.indexOf(row);
            this.listData.splice(i, 1);
            const parentIds = row.parentIds ? row.parentIds + '::' + row.id : row.id;
            while (true) {
                const index = this.listData.findIndex(value => value.parentIds === parentIds);
                if (index < 0) {
                    break;
                } else {
                    this.listData.splice(index, 1);
                }
            }
            if (row.parent) {
                const parent = this.listData.find(value => value.id === row.parent.id);
                if (parent) {
                    parent.leaf = this.listData.filter(value => value.parentIds === row.parentIds).length === 0;
                }
            }
        });
        this.listData = [...this.listData];
    }

    expandChange(row: any): void {
        if (!row.loaded) {
            let index = this.listData.findIndex(value => value.id === row.id);
            this.entity.findAllByParent(row, this.filterForm, {
                success: (res: any) => {
                    row.loaded = true;
                    row.expand = true;
                    res.forEach((child: any) => {
                        child.parent = null;
                        this.listData.splice(++index, 0, child);
                        this.listData = [...this.listData];
                    });
                }
            });
        }
    }

}
