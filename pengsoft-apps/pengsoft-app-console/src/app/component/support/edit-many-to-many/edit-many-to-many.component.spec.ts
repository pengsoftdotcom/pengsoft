import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EditManyToManyComponent } from './edit-many-to-many.component';


describe('EditManyToManyComponent', () => {
    let component: EditManyToManyComponent;
    let fixture: ComponentFixture<EditManyToManyComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [EditManyToManyComponent]
        })
            .compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(EditManyToManyComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
