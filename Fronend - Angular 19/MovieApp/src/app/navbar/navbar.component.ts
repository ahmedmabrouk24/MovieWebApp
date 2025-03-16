import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MovieService } from '../service/movie/movie.service';
import { AdminMovieService } from '../service/admin service/admin-service.service';
import { UserMovieService } from '../service/user service/user-service.service';
import { Router } from '@angular/router';
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
  isLoggedIn: boolean = false; // Track if the user is logged in
  searchText: string = ''; // For search input
  accessToken: string ='';

  constructor(
    private movieService: MovieService,
    private adminMovieService: AdminMovieService,
    private userMovieService: UserMovieService,
    private router: Router,
    private authService: AuthService,
    @Inject(PLATFORM_ID) private platformId: Object) {}

    ngOnInit(): void {
      this.checkLoginStatus(); // Check login status when the component initializes
      this.fetchSearchText(); // Continue to fetch search text as before
    }
    
  fetchSearchText(): void {
    console.log("updated :" + this.searchText);
    this.movieService.setSearchText(this.searchText); 
    this.adminMovieService.setSearchText(this.searchText);  
    this.userMovieService.setSearchText(this.searchText);  
  }

  checkLoginStatus(): void {
    if (isPlatformBrowser(this.platformId)) {
      // Check if a specific key exists in localStorage (e.g., 'accessToken')
      if (localStorage.getItem('accessToken')) {
        this.isLoggedIn = true;
      } else {
        this.isLoggedIn = false;
      }
    } else {
      this.isLoggedIn = false;  // Assume user is not logged in if not in the browser
    }
  }

  // Methods to handle login, signup, and logout
  login(): void {
    console.log(this.searchText);
    console.log('Login clicked');
    this.isLoggedIn = true;
    this.router.navigate(['/login']);  // Navigate to the login page
  }

  signup(): void {
    console.log('Sign up clicked');
    this.router.navigate(['/signup']);
  }

  logout(): void {
    console.log('Logout clicked');
    
    // Get the refreshToken from localStorage
    const refreshToken = this.getRefreshToken();
    console.log("the refresh token is " + refreshToken)
    if (refreshToken) {
      // Call the logout API via AuthService
      this.authService.logout(refreshToken).subscribe({
        next: (response) => {
          console.log('Logout API response:', response);
          // Clear localStorage on successful logout
          localStorage.clear();

          this.isLoggedIn = false;

          // Navigate to the home page after successful logout
          this.router.navigate(['']);
        },
        error: (error) => {
          console.error('Error during logout API call:', error);
          // Log the full error to get more details
          if (error instanceof HttpErrorResponse) {
            console.error('Error status:', error.status);
            console.error('Error message:', error.message);
            console.error('Error body:', error.error);
          }
        }
      });
    } else {
      console.log('No refreshToken found in localStorage');
      // If no refreshToken, just clear the localStorage and log the user out
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
      return token ? token : null;  // Returns the token if found, or null if not found
    }
    return null;  // Return null if not in the browser
  }
  
}

