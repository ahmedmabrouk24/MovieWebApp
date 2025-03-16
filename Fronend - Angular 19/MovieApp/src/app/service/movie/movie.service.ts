import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MovieService {
  
  private searchTextSubject = new BehaviorSubject<string>(''); // Holds the search text
  searchText$ = this.searchTextSubject.asObservable();

  constructor(private http:HttpClient) { }

  // Method to update the search text
  setSearchText(searchText: string): void {
    this.searchTextSubject.next(searchText); // Update the search text
  }

  getMoviesList(page: number){
    const url = `http://localhost:8080/user/api/v1/movies/unauth/${page}/get-all`;
    console.log("call : " + url);
    return this.http.get<any>(url);
  }
  getMoviesByTitle(movieTitle: string, page: number){
    const url = `http://localhost:8080/user/api/v1/movies/unauth/get-by-title/${page}/${movieTitle}`;
    console.log("call : " + url);
    return this.http.get<any>(url);
  }
  getMovieDetails(movieId: string){
    const url = `http://localhost:8080/user/api/v1/movies/unauth/get-by-id/${movieId}`;
    console.log("call : " + url);
    return this.http.get<any>(url);
  }
}
