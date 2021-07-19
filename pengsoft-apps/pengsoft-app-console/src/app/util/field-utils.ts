import { DomSanitizer } from '@angular/platform-browser';
import { NzCascaderOption } from 'ng-zorro-antd/cascader';
import { Edit } from '../component/support/form-item/edit';
import { Field } from '../component/support/form-item/field';
import { Input } from '../component/support/form-item/input';
import { Label } from '../component/support/form-item/label';
import { List } from '../component/support/form-item/list';
import { InputType } from '../enum/input-type.enum';
import { RegionService } from '../service/basedata/region.service';
import { DateUtils } from './date-utils';
import { EntityUtils } from './entity-utils';

export class FieldUtils {

    static buildCaptcha(field?: Field): Field {
        field = Object.assign({ code: 'captcha' }, field);
        field = this.getEdit(field, {
            label: { visible: false },
            input: { type: InputType.CAPTCHA, prefixIcon: 'lock', placeholder: '录入验证码' }
        });
        return field;
    }

    static buildJson(field?: Field): Field {
        field = this.getList(field, { visible: false });
        field = this.getEdit(field, { input: { type: InputType.JSON } });
        return field;
    }

    static buildAvatar(field?: Field): Field {
        field = Object.assign({ code: 'avatar', name: '头像' }, field);
        field = this.getList(field, { visible: false });
        field = this.getEdit(field, { input: { width: 100, height: 100, type: InputType.AVATAR } });
        return field;
    }

    static buildTextForBusinessKey(): Field {
        return this.buildText({
            code: 'businessKey', name: '业务键',
            list: { sortable: true },
            edit: { label: { tooltip: '外部系统中的唯一标志符' } },
            filter: {}
        });
    }

    static buildTextForCode(list?: List): Field {
        return this.buildText({
            code: 'code', name: '编码',
            list: Object.assign({ sortable: true, sortPriority: 1, }, list),
            edit: { required: true },
            filter: {}
        });
    }

    static buildTextForName(field?: Field): Field {
        field = Object.assign({ code: 'name', name: field && field.name ? field.name : '名称' }, field);
        field = this.getList(field, { sortable: true });
        field = this.getEdit(field, { required: true });
        field.filter = {};
        return this.buildText(field);
    }

    static buildText(field: Field): Field {
        field = this.getList(field);
        field = this.getEdit(field, { input: { type: InputType.TEXT } });
        return field;
    }

    static buildTextareaForRemark(): Field {
        return this.buildTextarea({ code: 'remark', name: '备注' });
    }

    static buildTextarea(field: Field): Field {
        field = this.getList(field, { visible: false });
        field = this.getEdit(field, { input: { type: InputType.TEXTAREA, rows: 4 } });
        return field;
    }

    static buildNumber(field: Field): Field {
        field = this.getList(field, { align: 'right' });
        field = this.getEdit(field, { input: { type: InputType.NUMBER } });
        return field;
    }

    static buildPassword(field: Field): Field {
        field = this.getList(field, { visible: false });
        field = this.getEdit(field, { input: { type: InputType.PASSWORD } });
        return field;
    }

    static buildSelect(field: Field): Field {
        field = this.getList(field, { render: (f: Field, row: any) => row[f.code] ? row[f.code].name : null });
        field = this.getEdit(field, { input: { lazy: false, multiple: false, options: [], placeholder: '请选择', type: InputType.SELECT } });
        return field;
    }

    static buildTreeSelect(field: Field): Field {
        field = this.getList(field, { render: (f: Field, row: any) => row[f.code] ? row[f.code].name : null });
        field = this.getEdit(field, {
            input: {
                lazy: false, multiple: false, options: [], placeholder: '请选择', type: InputType.TREE_SELECT
            }
        });
        return field;
    }

    static buildCascader(field: Field): Field {
        field = this.getList(field, { render: (f: Field, row: any) => row[f.code] ? row[f.code].name : null });
        field = this.getEdit(field, { input: { lazy: false, multiple: false, options: [], placeholder: '请选择', type: InputType.CASCADER } });
        return field;
    }

    static buildCascaderForRegion(region: RegionService, field?: Field): Field {
        field = this.getList(field, {
            render: (f: Field, row: any) => {
                const parents = [];
                let parent = row.region;
                while (parent) {
                    parents.push(parent);
                    parent = parent.parent;
                }
                parents.reverse();
                return parents.map(p => p.name).join('');
            }
        });
        field = this.getEdit(field, {
            input: {
                changeOnSelect: true,
                lazy: true,
                load: (node: NzCascaderOption, index: number) => new Promise<void>(resolve => {
                    let parent = null;
                    if (index > -1) {
                        parent = node.value;
                    }
                    region.findAllExcludeSelfAndItsChildrenByParent(parent, null, null, {
                        success: (res: any) => {
                            node.children = EntityUtils.convertListToTree(res);
                            resolve();
                        }
                    });
                })
            }
        });
        field = Object.assign({ code: 'region', name: '行政区划' }, field);
        return this.buildCascader(field);
    }

    static buildDatetimeForExpiredAt(): Field {
        return this.buildDatetime({
            code: 'expiredAt', name: '过期时间',
            list: {
                render: (field: Field, row: any, sanitizer: DomSanitizer) => {
                    const expiredAt = row[field.code];
                    if (expiredAt) {
                        if (DateUtils.isAfter(expiredAt)) {
                            return sanitizer.bypassSecurityTrustHtml(`<span style="color: #0b8235">${expiredAt}</span>`);
                        } else {
                            return sanitizer.bypassSecurityTrustHtml(`<span style="color: #ff4d4f">${expiredAt}</span>`);
                        }
                    }
                    return null;
                }
            },
            filter: { input: { placeholder: '早于录入的时间' } }
        });
    }

    static buildDatetime(field: Field): Field {
        field = this.getList(field, { width: 170, align: 'center' });
        field = this.getEdit(field, { input: { placeholder: '请选择', type: InputType.DATETIME } });
        return field;
    }

    static buildDate(field: Field): Field {
        field = this.getList(field, { width: 170, align: 'center' });
        field = this.getEdit(field, { input: { placeholder: '请选择', type: InputType.DATE } });
        return field;
    }

    static buildBooleanForEnabled(field?: Field): Field {
        field = Object.assign({
            code: 'enabled', name: '是否启用', list: {
                render: (f: Field, row: any, sanitizer: DomSanitizer) => {
                    const enabled = row.enabled;
                    if (enabled) {
                        return sanitizer.bypassSecurityTrustHtml('<span style="color: #0b8235">启用</span>');
                    } else {
                        return sanitizer.bypassSecurityTrustHtml('<span style="color: #ff4d4f">禁用</span>');
                    }
                }
            }
        }, field);
        return this.buildBoolean(field);
    }

    static buildBooleanForLocked(field?: Field): Field {
        field = Object.assign({
            code: 'locked', name: '是否锁定',
            list: {
                render: (f: Field, row: any, sanitizer: DomSanitizer) => {
                    if (row[field.code]) {
                        return sanitizer.bypassSecurityTrustHtml('<span style="color: #0b8235">是</span>');
                    } else {
                        return sanitizer.bypassSecurityTrustHtml('<span style="color: #ff4d4f">否</span>');
                    }
                }
            }
        }, field);
        return this.buildBoolean(field);
    }

    static buildBoolean(field: Field): Field {
        field = this.getList(field, { width: 80, align: 'center' });
        field = this.getEdit(field, { input: { type: InputType.BOOLEAN } });
        return field;
    }

    static getList(field: Field, list?: List): Field {
        list = Object.assign({
            filterable: false,
            sortable: false,
            sortPriority: 99,
            visible: true,
            childrenVisible: true,
            align: 'left'
        }, list);
        if (!field) {
            field = {};
        }
        field.list = Object.assign(list, field.list);
        if (!field.list.code) {
            field.list.code = field.code;
        }
        return field;
    }

    static getEdit(field: Field, edit?: Edit): Field {
        field.edit = Object.assign({ visible: true }, field.edit);
        if (edit) {
            for (const key in edit) {
                if (edit.hasOwnProperty(key) && key !== 'label' && key !== 'input' && field.edit[key] === undefined) {
                    field.edit[key] = edit[key];
                }
            }
            field = edit.label ? this.getLabel(field, edit.label) : this.getLabel(field);
            field = edit.input ? this.getInput(field, edit.input) : this.getInput(field);
        } else {
            field = this.getLabel(field);
            field = this.getInput(field);
        }
        field.edit.code = !field.edit.code ? field.code : null;
        if (field.filter) {
            const label = Object.assign({}, field.edit.label);
            const input = Object.assign({}, field.edit.input);
            field.filter.label = Object.assign(label, field.filter.label);
            field.filter.input = Object.assign(input, field.filter.input);
        }
        return field;
    }

    static getLabel(field: Field, label?: Label): Field {
        field.edit = Object.assign({}, field.edit);
        field.edit.label = Object.assign({ span: 4 }, field.edit.label);
        if (label) {
            field.edit.label = Object.assign(label, field.edit.label);
        }
        return field;
    }

    static getInput(field: Field, input?: Input): Field {
        field.edit = Object.assign({}, field.edit);
        field.edit.input = Object.assign({ span: 20 }, field.edit.input);
        if (input) {
            field.edit.input = Object.assign(input, field.edit.input);
        }
        return field;
    }

}