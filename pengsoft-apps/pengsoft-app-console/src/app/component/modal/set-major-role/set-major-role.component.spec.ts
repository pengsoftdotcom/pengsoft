import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { SetPrimaryRoleComponent } from '../set-primary-role/set-primary-role.component';



describe('SetPrimaryRoleComponent', () => {
    let component: SetPrimaryRoleComponent;
    let fixture: ComponentFixture<SetPrimaryRoleComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [SetPrimaryRoleComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(SetPrimaryRoleComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
