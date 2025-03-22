import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { timer } from 'rxjs';
import { isPlatformBrowser } from '@angular/common';

@Injectable({
  providedIn: 'root',
})
export class ServerStatusService {
  private readonly healthCheckUrl = '/api/health'; 

  constructor(
    private http: HttpClient,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  startHealthCheck(interval: number = 5000): void {
    timer(0, interval)
      .pipe(
        switchMap(() => this.checkServerStatus()),
        catchError(() => {
          this.clearLocalStorage(); 
          return of(null); 
        })
      )
      .subscribe();
  }

  private checkServerStatus(): Observable<any> {
    return this.http.get(this.healthCheckUrl);
  }

  private clearLocalStorage(): void {
    /*if (isPlatformBrowser(this.platformId)) {
      console.log('Server is down. Clearing localStorage.');
      localStorage.clear(); 
    } else {
      console.log('Not in the browser. localStorage will not be cleared.');
    }*/
  }
}
