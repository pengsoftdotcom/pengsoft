import { format, isAfter, parse } from 'date-fns';

export class DateUtils {

    static formatDate(date: any): string {
        return format(date, 'yyyy-MM-dd');
    }

    static formatTime(time: any): string {
        return format(time, 'HH:mm:ss');
    }

    static formatDateTime(dateTime: any): string {
        return format(dateTime, 'yyyy-MM-dd HH:mm:ss');
    }

    static parseDate(date: string): Date {
        return parse(date, 'yyyy-MM-dd', new Date());
    }

    static parseTime(time: string): Date {
        return parse(time, 'HH:mm:ss', new Date());
    }

    static parseDatetime(dateTime: string): Date {
        return parse(dateTime, 'yyyy-MM-dd HH:mm:ss', new Date());
    }

    static isAfter(dateTime: string): boolean {
        return isAfter(this.parseDatetime(dateTime), new Date());
    }

}
