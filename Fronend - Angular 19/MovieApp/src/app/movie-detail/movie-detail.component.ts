import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MovieService } from '../service/movie/movie.service';
import { AdminMovieService } from '../service/admin service/admin-service.service';
import { Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { Location } from '@angular/common';
import { Subscription } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-movie-detail',
  imports:[FormsModule, CommonModule],
  templateUrl: './movie-detail.component.html',
  styleUrls: ['./movie-detail.component.scss']
})
export class MovieDetailComponent implements OnInit, OnDestroy {
  movieDetails: any;
  previousRoute: string = '';
  accessToken: string = '';
  private routeSubscription: Subscription = new Subscription();

  constructor(
    private route: ActivatedRoute, 
    private router: Router,
    private movieService: MovieService,
    private adminMovieService: AdminMovieService,
    private location: Location,  
    @Inject(PLATFORM_ID) private platformId: Object) {}

  ngOnInit(): void {
    this.routeSubscription = this.route.paramMap.subscribe((params) => {
      const movieId = params.get('imdbID');
      this.fetchMovieDetails(movieId);
    });
  }

  ngOnDestroy(): void {
    if (this.routeSubscription) {
      this.routeSubscription.unsubscribe();
    }
  }

  fetchMovieDetails(movieId: string | null): void {
    if (!movieId) {
      console.error('Invalid movie imdbID');
      return;
    }

    if (this.accessToken.length > 0) { 
      console.log("ADMIN");
      this.getToken();
      this.adminMovieService.getMovieById(movieId, this.accessToken).subscribe(
        (data) => {
          this.movieDetails = data;
        },
        (error) => console.error(error)
      );
    } else { 
      console.log("USER");
      this.movieService.getMovieDetails(movieId).subscribe(
        (data) => {
          this.movieDetails = data;
        },
        (error) => console.error(error)
      );
    }
  }

  getToken(): void {
    if (isPlatformBrowser(this.platformId)) {
      const token = localStorage.getItem('accessToken');
      if (token) {
        this.accessToken = token;  
      }
    }
  }

  goBack(): void {
    if (this.previousRoute && this.previousRoute !== '/movie-detail') {
      this.router.navigate([this.previousRoute]);
    } else {
      this.location.back(); 
    }
  }
}
