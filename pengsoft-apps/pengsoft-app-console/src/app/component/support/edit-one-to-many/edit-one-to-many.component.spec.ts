import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EditOneToManyComponent } from './edit-one-to-many.component';


describe('EditOneToManyComponent', () => {
    let component: EditOneToManyComponent;
    let fixture: ComponentFixture<EditOneToManyComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [EditOneToManyComponent]
        })
            .compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(EditOneToManyComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
