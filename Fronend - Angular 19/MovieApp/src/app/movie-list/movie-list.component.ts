import { Component } from '@angular/core';
import { MovieCardComponent } from '../movie-card/movie-card.component';
import { CommonModule } from '@angular/common';
import { MovieService } from '../service/movie/movie.service';
import { FormsModule } from '@angular/forms';  
import { Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { AuthService } from '../service/auth/auth.service';
import { Router } from '@angular/router';
import { AdminMovieService } from '../service/admin service/admin-service.service';
@Component({
  selector: 'app-movie-list',
  imports: [MovieCardComponent, CommonModule, FormsModule],
  templateUrl: './movie-list.component.html',
  styleUrls: ['./movie-list.component.scss']
})
export class MovieListComponent {
  movies: any[] = [];
  searchText: string = '';
  currentPage: number = 1;
  resultsPerPage: number = 12;  
  totalResults: number = 0;     
  totalPages: number = 0;       
  imdbID: string = ''; 
  movieTitle: string = '';
  accessToken: string = '';
  isAdmin: boolean = false;
  

  constructor(
    private movieService: MovieService,
    @Inject(PLATFORM_ID) private platformId: Object) {}

  ngOnInit(): void {
    this.getToken();
    this.movieService.searchText$.subscribe(searchText => {
      this.searchText = searchText;
      console.log('Search Text from movie list Updated: ', this.searchText);
      this.fetchMovies();
    });
  }


  validateImdbID(imdbID: string): boolean {
    return imdbID.startsWith('tt') && imdbID.length === 9;
  }

  fetchMovies(): void {
    if (this.searchText === ''){
      this.movieService.getMoviesList(this.currentPage).subscribe(response => {
        if (response && response.Search) {
          this.movies = response.Search;  
          this.totalResults = response.totalResults;  
          this.totalPages = Math.ceil(this.totalResults / this.resultsPerPage); 
        }
      });
    }
    else{
      this.movieTitle = this.searchText;
      this.searchMoviesByTitle();
    }
  }

  searchMoviesByTitle() {
    if (this.movieTitle) {
      this.movieService.getMoviesByTitle(this.searchText, this.currentPage).subscribe(response => {
        this.movies = response.Search;  
        this.totalResults = response.totalResults;  
        this.totalPages = Math.ceil(this.totalResults / this.resultsPerPage); 
      });
    }
  }
  onNextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.fetchMovies();
    }
  }

  onPreviousPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.fetchMovies();
    }
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.fetchMovies();
  }

  getToken(): void{
    if (isPlatformBrowser(this.platformId)) {
      const token = localStorage.getItem('accessToken');
      if (token) {
        this.accessToken = token;  
      } 
    }
  }

}
