import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SelectConsumerComponent } from './select-consumer.component';


describe('SelectConsumerComponent', () => {
    let component: SelectConsumerComponent;
    let fixture: ComponentFixture<SelectConsumerComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [SelectConsumerComponent]
        })
            .compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(SelectConsumerComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
