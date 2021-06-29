import { Directive, ElementRef, Input, OnInit } from '@angular/core';
import { SecurityService } from '../service/support/security.service';

@Directive({
    // tslint:disable-next-line: directive-selector
    selector: '[hasAnyAuthority]'
})
export class HasAnyAuthorityDirective implements OnInit {

    @Input('hasAnyAuthority') authorityCodes: string;

    @Input('title') exclusive: string;

    constructor(private el: ElementRef, private security: SecurityService) { }

    ngOnInit(): void {
        if (this.security.hasAnyAuthority(this.authorityCodes) && this.exclusive && this.security.hasAnyAuthority(this.exclusive)
            || !this.security.hasAnyAuthority(this.authorityCodes)) {
            this.el.nativeElement.remove();
        }
    }

}
