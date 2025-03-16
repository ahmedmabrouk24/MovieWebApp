import { Component, OnInit } from '@angular/core';
import { UserMovieService } from '../service/user service/user-service.service';
import { ActivatedRoute } from '@angular/router';  // For routing parameters (if needed)
import { Observable } from 'rxjs';
import { CommonModule} from '@angular/common';
import { MovieCardComponent } from '../movie-card/movie-card.component';
import { FormsModule } from '@angular/forms';
import { isPlatformBrowser } from '@angular/common';
import { Inject, PLATFORM_ID } from '@angular/core';


@Component({
  selector: 'app-user-page',
  imports: [MovieCardComponent, CommonModule, FormsModule],
  templateUrl: './user-page.component.html',
  styleUrls: ['./user-page.component.scss']
})
export class UserPageComponent implements OnInit {
  movies: any[] = [];
  searchText: string = '';
  accessToken: string = ''; 
  currentPage: number = 1;
  totalPages: number = 0;
  resultsPerPage: number = 12;
  totalResults: number = 0;   
  isUser: boolean = true;

  constructor(
    private userMovieService: UserMovieService, 
    private activatedRoute: ActivatedRoute,
    @Inject(PLATFORM_ID) private platformId: Object) { }

  ngOnInit(): void {
    this.getToken();
    this.fetchMovies();

    console.log("access token from admin page : " + this.accessToken);
    this.userMovieService.searchText$.subscribe(searchText => {
      this.searchText = searchText;
      console.log('Search Text at user Updated: ', this.searchText);

      this.fetchMovies();
    });

  }

  fetchMovies(): void {
    if (this.searchText === '') {
      this.userMovieService.getAllMovies(this.currentPage, this.accessToken).subscribe(response => {
        if (response && response.Search) {
          this.movies = response.Search;  
          this.totalResults = response.totalResults;  
          this.totalPages = Math.ceil(this.totalResults / this.resultsPerPage); 
        }
      });
    } else {
      this.userMovieService.getMoviesByTitle(this.currentPage, this.searchText, this.accessToken).subscribe(response => {
        if (response && response.Search) {
          this.movies = response.Search;  
          this.totalResults = response.totalResults;  
          this.totalPages = Math.ceil(this.totalResults / this.resultsPerPage);  
        }
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
