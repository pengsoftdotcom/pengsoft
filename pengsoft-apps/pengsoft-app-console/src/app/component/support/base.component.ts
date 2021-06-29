import { Injectable, Input } from '@angular/core';

@Injectable()
export abstract class BaseComponent {

    title = '';

    width = '100%';

    height = '100%';

    visible = true;

    loading = false;

    show(): void {
        this.visible = true;
    }

    hide(): void {
        this.visible = false;
    }

}
