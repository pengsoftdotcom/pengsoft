import { DomSanitizer, SafeHtml } from "@angular/platform-browser";
import { NzTreeNodeOptions } from "ng-zorro-antd/tree";

export interface Option extends NzTreeNodeOptions {
    label: string;
    value: any;
    disabled?: boolean;
    hide?: boolean;
    customContent?: boolean;
    customRender?: (sanitizer: DomSanitizer) => string | SafeHtml;
    checked?: boolean;
}