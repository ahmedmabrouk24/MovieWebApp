import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) { }

  login(email: string, password: string): Observable<any> {
    const url = 'http://localhost:8080/api/v1/auth/login';
    const body = { email, password };
    return this.http.post<any>(url, body);
  }

  signup(userName: string, email: string, password: string): Observable<any> {
    const url = 'http://localhost:8080/api/v1/auth/signup';
    console.log("call : " + url);
    const body = { userName, email, password }; 
    return this.http.post(url, body, { responseType: 'text' });
  }

  isUser(accessToken: string): Observable<boolean> {
    const url = 'http://localhost:8080/user/api/v1/movies/unauth/isUser';
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${accessToken}`
    });
    return this.http.get<boolean>(url, { headers });
  }
  
  logout(refreshToken: string): Observable<any> {
  const url = 'http://localhost:8080/api/v1/auth/logout';
  const headers = new HttpHeaders({
    'Authorization': `Bearer ${refreshToken}`
  });

  return this.http.post(url, {}, { headers, responseType: 'text'});
  }
}
