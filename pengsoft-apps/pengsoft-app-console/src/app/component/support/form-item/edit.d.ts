import { Input } from "./input";
import { Label } from "./label";


export interface Edit {

    code?: string;

    required?: boolean;

    disabled?: boolean | ((form: any, edit: Edit) => boolean);

    visible?: boolean | ((form: any, edit: Edit) => boolean);

    childrenVisible?: boolean | ((form: any, edit: Edit) => boolean);

    label?: Label;

    input?: Input;

}