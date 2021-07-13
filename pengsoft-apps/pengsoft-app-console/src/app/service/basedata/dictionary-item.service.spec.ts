import { TestBed } from '@angular/core/testing';
import { DictionaryItemService } from './dictionary-item.service';


describe('DictionaryItemService', () => {
    let service: DictionaryItemService;

    beforeEach(() => {
        TestBed.configureTestingModule({});
        service = TestBed.inject(DictionaryItemService);
    });

    it('should be created', () => {
        expect(service).toBeTruthy();
    });
});
