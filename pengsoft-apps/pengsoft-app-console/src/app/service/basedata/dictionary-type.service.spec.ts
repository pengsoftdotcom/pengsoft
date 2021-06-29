import { TestBed } from '@angular/core/testing';

import { DictionaryTypeService } from './dictionary-type.service';

describe('DictionaryTypeService', () => {
  let service: DictionaryTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DictionaryTypeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
