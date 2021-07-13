import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SwitchOrganizationComponent } from './switch-organization.component';


describe('SwitchOrganizationComponent', () => {
    let component: SwitchOrganizationComponent;
    let fixture: ComponentFixture<SwitchOrganizationComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [SwitchOrganizationComponent]
        })
            .compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(SwitchOrganizationComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
