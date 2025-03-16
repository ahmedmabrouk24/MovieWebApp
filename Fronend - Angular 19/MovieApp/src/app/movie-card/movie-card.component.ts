import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { isPlatformBrowser } from '@angular/common';
import { Inject, PLATFORM_ID } from '@angular/core';
import { AdminMovieService } from '../service/admin service/admin-service.service';
import { UserMovieService } from '../service/user service/user-service.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-movie-card',
  imports: [CommonModule, FormsModule],
  templateUrl: './movie-card.component.html',
  styleUrls: ['./movie-card.component.scss']
})
export class MovieCardComponent implements OnInit {
  @Input() movie: any;
  @Input() isAdmin: boolean = false;
  @Input() isUser: boolean = false;
  @Input() isAdminAdd: boolean = false; 
  @Input() selectedMovies: Set<string> = new Set();
  @Output() toggleSelection = new EventEmitter<string>();  // Emit the imdbID when the selection changes

  isAdded: boolean = false ;
  accessToken: string = '';
  clickButton: boolean = false;

  constructor(
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object,
    private adminMovieService: AdminMovieService,
    private userMovieService: UserMovieService) {}

  ngOnInit(): void {
    this.getToken();
  }

  // Check if movie is selected
  isSelected(imdbID: string): boolean {
    return this.selectedMovies.has(imdbID);
  }

  handleSelection() {
    this.toggleSelection.emit(this.movie.imdbID);  // Emit the imdbID to the parent
  }

  onCardClick(event: MouseEvent): void {
    if (this.clickButton == false){
      console.log("Film is clicked", event);
      this.router.navigate(['/movie-detail', this.movie.imdbID]);
    }
    this.clickButton = false;
  }

  rateMovie(imdbID: string, rating: number): void {
    this.movie.userRating = rating;
    this.userMovieService.rateMovie(imdbID, rating, this.accessToken).subscribe({
      next: (response) => {
        console.log('API Response:', response);
      },
      error: (err) => {
        console.error('Error:', err);
      }
    });
    console.log('Movie rated:', rating); 
  }

  deleteMovie(imdbID: string): void {
    this.clickButton = true;
      if (this.isAdmin){
      console.log('Deleting movie with IMDB ID: ', imdbID);
      console.log('is admin -> ' + this.isAdmin);
      console.log('is admin add -> ' + this.isAdminAdd);

      this.adminMovieService.deleteMovie(imdbID, this.accessToken).subscribe({
        next: (response) => {
          console.log('Movie deleted successfully:', response);
        },
        error: (error) => {
          console.error('Error deleting movie:', error);
        }
      });
      window.location.reload();
    }
    else if (this.isAdminAdd){
      this.addMovieButton(imdbID);
    }
  }

  // Add movie by IMDb ID
  addMovieButton(imdbID: string): void{
    console.log('ADDing movie with IMDB ID: ', imdbID);
    this.adminMovieService.addMovie(imdbID, this.accessToken)
    .subscribe({
      next: (response: string) => {
        console.log('Movie added:', response);
      },
      error: (error) => {
        console.error('Error adding movie:', error);
      }
    });
    this.isAdded = true;
  }

  getToken(): void {
    if (isPlatformBrowser(this.platformId)) {
      const token = localStorage.getItem('accessToken');
      if (token) {
        this.accessToken = token;
      }
    }
  }
}
