import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../service/auth/auth.service';
import { Router } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  loginForm: FormGroup;
  errorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,  // Inject AuthService
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]]
    });
  }

  onLogin(): void {
    if (this.loginForm.invalid) {
      return;
    }

    const { email, password } = this.loginForm.value;

    this.authService.login(email, password).subscribe({
      next: (response) => {
        console.log(response);
        localStorage.setItem('accessToken', response.accessToken);
        localStorage.setItem('refreshToken', response.refreshToken);
        console.log(localStorage.getItem('accessToken'));
        console.log(localStorage.getItem('refreshToken'));

        this.authService.isUser(response.accessToken).subscribe({
          next: (isUser: boolean) => {
            localStorage.setItem('isUser', JSON.stringify(isUser));
             if (isUser) {
              console.log("User logged in successfully!")
              this.router.navigate(['/user-page']); 
            } else {
              console.log("Admin logged in successfully!")
              this.router.navigate(['/admin-page']); 
            }
          },
          error: (error) => {
            console.error('Error checking isAdmin:', error);
            this.errorMessage = 'Failed to verify admin status.';
          }
        });
      },
      error: (error) => {
        this.errorMessage = 'Login failed. Please check your credentials.';
      }
    });
  }
}
