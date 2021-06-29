import { Sort } from "./sort";

export interface Page {

    page: number;

    size: number;

    total?: number;

    sort?: Array<Sort>;

}