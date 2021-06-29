import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class StoreService {

    public readonly CACHE = 'pengsoft';

    public set(key: string, val: any): void {
        const cache = localStorage.getItem(this.CACHE) ? JSON.parse(localStorage.getItem(this.CACHE)) : {};
        cache[key] = val;
        localStorage.setItem(this.CACHE, JSON.stringify(cache));
    }

    public get(key: string): any {
        const cache = localStorage.getItem(this.CACHE) ? JSON.parse(localStorage.getItem(this.CACHE)) : {};
        return cache[key];
    }

    public clear(): void {
        localStorage.removeItem(this.CACHE);
    }

}
