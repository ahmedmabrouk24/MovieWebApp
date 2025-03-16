import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AdminMovieService {

  private apiUrl = 'http://localhost:8080/admin/api/v1/movies'; // Base URL for admin movie endpoints

  private searchTextSubject = new BehaviorSubject<string>(''); // Holds the search text
    searchText$ = this.searchTextSubject.asObservable();
  
  constructor(private http:HttpClient) { }


  // Method to update the search text
  setSearchText(searchText: string): void {
    this.searchTextSubject.next(searchText); // Update the search text
  }
  

  // Fetch movie by IMDb ID
  getMovieById(imdbID: string, accessToken: string): Observable<any> {
    const url = `${this.apiUrl}/get-by-id/${imdbID}`;
    console.log("call -> " + url)
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${accessToken}`
    });
    return this.http.get<any>(url, { headers });
  }

  // Fetch movies by title with pagination
  getMoviesByTitle(pageNo: number, movieTitle: string, accessToken: string): Observable<any> {
    const url = `${this.apiUrl}/get-by-title/${pageNo}/${movieTitle}`;
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${accessToken}`
    });
    return this.http.get<any>(url, { headers });
  }

  // Add movie by IMDb ID
  addMovie(imdbID: string, accessToken: string): Observable<string> {
    const url = `${this.apiUrl}/add-movie/${imdbID}`;
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${accessToken}`
    });
    return this.http.post<string>(url, {}, { headers, responseType: 'text' as 'json' });
  }

  // Delete movie by IMDb ID
  deleteMovie(imdbID: string, accessToken: string): Observable<string> {
    const url = `${this.apiUrl}/delete-movie/${imdbID}`;
    console.log("call : " + url);
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${accessToken}`
    });
    return this.http.delete<string>(url, { headers, responseType: 'text' as 'json' });
  }

  // Add movies in batch
  addMoviesBatch(imdbIDs: string[], accessToken: string): Observable<string> {
    const url = `${this.apiUrl}/addBatch`;
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${accessToken}`,
      'Content-Type': 'application/json' 
    });
    const options = { 
      headers,
      responseType: 'text' as 'json'
    };

    return this.http.post<string>(url, imdbIDs, options);  
  }

  removeMoviesBatch(imdbIDs: string[], accessToken: string): Observable<string> {
    const url = `${this.apiUrl}/removeBatch`;
  
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${accessToken}`,
      'Content-Type': 'application/json'  
    });
  
    const options = { 
      headers, 
      body: imdbIDs, 
      responseType: 'text' as 'json'  
    };
    return this.http.delete<string>(url, options); 
  }
}
