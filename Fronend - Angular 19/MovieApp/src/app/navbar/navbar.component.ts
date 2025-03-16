import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MovieService } from '../service/movie/movie.service';
import { AdminMovieService } from '../service/admin service/admin-service.service';
import { UserMovieService } from '../service/user service/user-service.service';
import { Router, NavigationEnd } from '@angular/router';
import { AuthService } from '../service/auth/auth.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

@Component({
  selector: 'app-navbar',
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {
  isLoggedIn: boolean = false; 
  searchText: string = ''; 
  accessToken: string ='';

  constructor(
    private movieService: MovieService,
    private adminMovieService: AdminMovieService,
    private userMovieService: UserMovieService,
    private router: Router,
    private authService: AuthService,
    @Inject(PLATFORM_ID) private platformId: Object) {}

    ngOnInit(): void {
      this.checkLoginStatus(); 
      this.fetchSearchText();

      this.router.events.subscribe(event => {
        if (event instanceof NavigationEnd) {
          this.checkLoginStatus(); 
          this.fetchSearchText();
        }
      });

    }
    
  fetchSearchText(): void {
    console.log("updated :" + this.searchText);
    this.movieService.setSearchText(this.searchText); 
    this.adminMovieService.setSearchText(this.searchText);  
    this.userMovieService.setSearchText(this.searchText);  
  }

  checkLoginStatus(): void {
    if (isPlatformBrowser(this.platformId)) {
      if (localStorage.getItem('accessToken')) {
        console.log("is logged in is true");
        this.isLoggedIn = true;
      } else {
        console.log("is logged in is false");
        this.isLoggedIn = false;
      }
    } else {
      this.isLoggedIn = false;  
    }
  }

  login(): void {
    console.log(this.searchText);
    console.log('Login clicked');
    this.router.navigate(['/login']);  
  }

  signup(): void {
    console.log('Sign up clicked');
    this.router.navigate(['/signup']);
  }

  logout(): void {
    console.log('Logout clicked');
    const refreshToken = this.getRefreshToken();
    console.log("the refresh token is " + refreshToken)
    if (refreshToken) {
      this.authService.logout(refreshToken).subscribe({
        next: (response) => {
          console.log('Logout API response:', response);
          localStorage.clear();

          this.isLoggedIn = false;
          this.router.navigate(['']);
        },
        error: (error) => {
          console.error('Error during logout API call:', error);
          if (error instanceof HttpErrorResponse) {
            console.error('Error status:', error.status);
            console.error('Error message:', error.message);
            console.error('Error body:', error.error);
          }
        }
      });
    } else {
      console.log('No refreshToken found in localStorage');
      localStorage.clear();

      this.isLoggedIn = false;
      this.router.navigate(['/']);
    }
  }

  getToken(): void{
    if (isPlatformBrowser(this.platformId)) {
      const token = localStorage.getItem('accessToken');
      if (token) {
        this.accessToken = token;  
      } 
    }
  }
  getRefreshToken(): string | null {
    if (isPlatformBrowser(this.platformId)) {
      const token = localStorage.getItem('refreshToken');
      return token ? token : null;  
    }
    return null;  
  }
  
}

