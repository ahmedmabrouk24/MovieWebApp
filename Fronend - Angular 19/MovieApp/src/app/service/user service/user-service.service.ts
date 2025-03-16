import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserMovieService {

  private apiUrl = 'http://localhost:8080/user/api/v1/movies'; // Base URL for your movie endpoints

  private searchTextSubject = new BehaviorSubject<string>(''); // Holds the search text
  searchText$ = this.searchTextSubject.asObservable();
  
  constructor(private http:HttpClient) { }
  
  // Method to update the search text
  setSearchText(searchText: string): void {
    this.searchTextSubject.next(searchText); // Update the search text
  }

  getMovieById(imdbID: string, accessToken: string): Observable<any> {
    const url = `${this.apiUrl}/get-by-id/${imdbID}`;
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${accessToken}`
    });
    return this.http.get<any>(url, { headers });
  }

  getMoviesByTitle(pageNo: number, movieName: string, accessToken: string): Observable<any> {
    console.log("seache 3nd el user");
    const url = `${this.apiUrl}/get-by-title/${pageNo}/${movieName}`;
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${accessToken}`
    });
    return this.http.get<any>(url, { headers });
  }

  getAllMovies(pageNo: number, accessToken: string): Observable<any> {
    const url = `${this.apiUrl}/${pageNo}/get-all`;
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${accessToken}`
    });
    return this.http.get<any>(url, { headers });
  }

  rateMovie(imdbID: string, rating: number, accessToken: string): Observable<any> {
    const url = `http://localhost:8080/user/api/v1/rate`;
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${accessToken}`
    });
    const params = new HttpParams()
      .set('imdbID', imdbID)
      .set('rating', rating.toString()); 
    return this.http.post<any>(url, null, { params, headers }); 
  }

}
