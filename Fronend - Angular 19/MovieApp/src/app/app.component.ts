import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MovieService } from './service/movie/movie.service';
import { MovieListComponent } from './movie-list/movie-list.component';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from './navbar/navbar.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MovieCardComponent } from './movie-card/movie-card.component';
import { AdminPageComponent } from './admin-page/admin-page.component';
import { UserPageComponent } from './user-page/user-page.component';

@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet, 
    MovieListComponent, 
    CommonModule,
    NavbarComponent,
    ReactiveFormsModule,
    MovieCardComponent,
    AdminPageComponent,
    UserPageComponent
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  constructor(private movieService: MovieService) {
  }
}
