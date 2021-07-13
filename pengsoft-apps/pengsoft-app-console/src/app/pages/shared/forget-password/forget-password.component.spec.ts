import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FullScreenForgetPasswordComponent } from './forget-password.component';


describe('ForgetPasswordComponent', () => {
    let component: FullScreenForgetPasswordComponent;
    let fixture: ComponentFixture<FullScreenForgetPasswordComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [FullScreenForgetPasswordComponent]
        })
            .compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(FullScreenForgetPasswordComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
