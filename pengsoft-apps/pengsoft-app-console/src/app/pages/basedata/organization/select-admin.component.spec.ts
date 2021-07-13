import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SelectAdminComponent } from './select-admin.component';


describe('SelectAdminComponent', () => {
    let component: SelectAdminComponent;
    let fixture: ComponentFixture<SelectAdminComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [SelectAdminComponent]
        })
            .compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(SelectAdminComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
