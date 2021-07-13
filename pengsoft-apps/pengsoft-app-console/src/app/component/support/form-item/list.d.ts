import { DomSanitizer, SafeHtml } from "@angular/platform-browser";
import { Field } from "./field";

export interface List {

    code?: string;

    sortable?: boolean;

    sortPriority?: number;

    visible?: boolean;

    childrenVisible?: boolean;

    align?: 'left' | 'center' | 'right';

    label?: string;

    width?: number;

    render?: (field: Field, row: any, sanitizer: DomSanitizer) => string | SafeHtml;

}