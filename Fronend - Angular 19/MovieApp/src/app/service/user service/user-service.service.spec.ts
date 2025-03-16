import { TestBed } from '@angular/core/testing';

import { UserMovieService } from './user-service.service';

describe('UserServiceService', () => {
  let service: UserMovieService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserMovieService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
