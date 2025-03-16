import { Component, OnInit } from '@angular/core';
import { AdminMovieService } from '../service/admin service/admin-service.service';
import { MovieService } from '../service/movie/movie.service';
import { HttpErrorResponse } from '@angular/common/http'; 
import { MovieCardComponent } from '../movie-card/movie-card.component';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { isPlatformBrowser } from '@angular/common';
import { Inject, PLATFORM_ID } from '@angular/core';

interface Movie {
  title: string;
  imdbID: string;
}

@Component({
  selector: 'app-admin-page',
  imports: [MovieCardComponent, CommonModule, FormsModule],
  templateUrl: './admin-page.component.html',
  styleUrls: ['./admin-page.component.scss']
})
export class AdminPageComponent implements OnInit {
  movies: any[] = [];
  movieTitle: string = '';
  imdbID: string = ''; 
  pageNo: number = 1;
  accessToken: string = ''; 
  batchImdbIDs: string[] = []; 
  searchText: string = '';
  currentPage: number = 1;
  resultsPerPage: number = 12;  
  totalResults: number = 0;     
  totalPages: number = 0;    
  isAdmin: boolean = true;
  isAdminAdd: boolean = false;
  selectedMovies: Set<string> = new Set();

  successMessage: string = '';
  showMessage: boolean = false;

  constructor(
    private adminMovieService: AdminMovieService,
    private movieService: MovieService,
    @Inject(PLATFORM_ID) private platformId: Object) {}

  ngOnInit(): void {
    console.log("Admin Page");
    this.getToken();
    // Subscribe to search text changes
    console.log("access token from admin page : " + this.accessToken);
    this.adminMovieService.searchText$.subscribe(searchText => {
      this.searchText = searchText;
      console.log('Search Text at admin Updated: ', this.searchText);
      this.currentPage = 1;
      this.fetchMovies();
    });
  }

  fetchMovies(): void {
    if (this.searchText === ''){
      this.isAdminAdd = false;
      this.isAdmin = true;
      this.getAllMovies();
    }
    else{
      this.movieTitle = this.searchText;
      console.log("search by title from admin");
      this.searchMoviesByTitle();
    }
  };

  removeSelectedMovies(): void {
    if (this.selectedMovies.size > 0) {
      const imdbIDs = Array.from(this.selectedMovies);  
      this.getToken();  
      console.log("array of ides : " + imdbIDs);
      this.adminMovieService.removeMoviesBatch(imdbIDs, this.accessToken).subscribe({
        next: (response) => {
          console.log(response);
          this.fetchMovies();
          this.selectedMovies.clear();
        },
        error: (error) => {
          console.error('Error removing movies:', error);
        }
      });
    } else {
      console.log('No movies selected to remove.');
    }
  }

  addSelectedMovies() {
    if (this.selectedMovies.size > 0) {
      const imdbIDs = Array.from(this.selectedMovies);  
      this.getToken();  
      console.log("array of ides : " + imdbIDs);
      console.log(this.accessToken);
      this.adminMovieService.addMoviesBatch(imdbIDs, this.accessToken).subscribe({
        next: (response) => {
          console.log('Movies added successfully:', response);
          this.selectedMovies.clear();
          this.showMessage = true;  
            setTimeout(() => {
              this.showMessage = false;
            }, 3000); 
        },
        error: (error) => {
          console.error('Error added movies:', error);
        }
      });
    } else {
      console.log('No movies selected to add.');
    }
  }

  toggleSelection(imdbID: string): void {
    if (this.selectedMovies.has(imdbID)) {
      console.log("delete from list");
      this.selectedMovies.delete(imdbID);  
    } else {
      console.log("add to list");
      this.selectedMovies.add(imdbID);  
    }
  }

  getAllMovies(){
    this.movieService.getMoviesList(this.currentPage).subscribe(response => {
      if (response && response.Search) {
        this.movies = response.Search;  
        this.totalResults = response.totalResults;  
        this.totalPages = Math.ceil(this.totalResults / this.resultsPerPage); 
      }
    });
  }
  
  searchMoviesByTitle() {
    this.isAdminAdd = true;
    this.isAdmin = false;
    if (this.movieTitle && this.accessToken) {
      this.adminMovieService.getMoviesByTitle(this.currentPage, this.searchText, this.accessToken).subscribe(response => {
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
