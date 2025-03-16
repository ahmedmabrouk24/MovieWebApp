import { TestBed } from '@angular/core/testing';

import { AdminMovieService } from './admin-service.service';

describe('AdminServiceService', () => {
  let service: AdminMovieService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdminMovieService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
