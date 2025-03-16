import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MovieDetailComponent } from './movie-detail/movie-detail.component';
import { MovieListComponent } from './movie-list/movie-list.component';
import { LoginComponent } from './login/login.component';
import { UserPageComponent } from './user-page/user-page.component';
import { AdminPageComponent } from './admin-page/admin-page.component';
import { SignupComponent } from './signup/signup.component';
export const routes: Routes = [
    { path: '', component: MovieListComponent },  
    { path: 'movie-detail/:imdbID', component: MovieDetailComponent },
    { path: 'login', component: LoginComponent }, 
    { path: 'user-page', component: UserPageComponent }, 
    { path: 'admin-page', component: AdminPageComponent},  
    { path: 'signup', component: SignupComponent},  
  ];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
