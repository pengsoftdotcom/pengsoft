import { TestBed } from '@angular/core/testing';
import { SupplierConsumerService } from './supplier-consumer.service';


describe('SupplierConsumerService', () => {
    let service: SupplierConsumerService;

    beforeEach(() => {
        TestBed.configureTestingModule({});
        service = TestBed.inject(SupplierConsumerService);
    });

    it('should be created', () => {
        expect(service).toBeTruthy();
    });
});
