import { InputType } from "src/app/enum/input-type.enum";
import { InputComponent } from "../input/input.component";
import { Edit } from "./edit";
import { Option } from './option';

export interface Input {

    visible?: boolean,

    placeholder?: string,

    type?: InputType,

    prefixIcon?: string,

    suffixIcon?: string,

    modelChange?: (component: InputComponent) => void;

    changeOnSelect?: boolean,

    options?: Array<Option>;

    optionLabelRender?: (edit: Edit, form: any) => string;

    loaded?: boolean;

    load?: (component: any, event?: any | number) => any;

    rows?: number;

    lazy?: boolean;

    multiple?: boolean;

    width?: number;

    height?: number;

}