import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FullScreenSignInComponent } from './sign-in.component';


describe('SignInComponent', () => {
    let component: FullScreenSignInComponent;
    let fixture: ComponentFixture<FullScreenSignInComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [FullScreenSignInComponent]
        })
            .compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(FullScreenSignInComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
