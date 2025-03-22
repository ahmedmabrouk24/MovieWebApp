import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MovieDetailComponent } from './movie-detail/movie-detail.component';
import { MovieListComponent } from './movie-list/movie-list.component';
import { LoginComponent } from './login/login.component';
import { UserPageComponent } from './user-page/user-page.component';
import { AdminPageComponent } from './admin-page/admin-page.component';
import { SignupComponent } from './signup/signup.component';
import { AuthGuard } from './auth.guard';

export const routes: Routes = [
    { path: '', component: MovieListComponent },  
    { path: 'movie-detail/:imdbID', component: MovieDetailComponent },
    { path: 'login', component: LoginComponent }, 
    { path: 'user-page', component: UserPageComponent, canActivate: [AuthGuard] }, 
    { path: 'admin-page', component: AdminPageComponent, canActivate: [AuthGuard]},  
    { path: 'signup', component: SignupComponent},  
  ];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
