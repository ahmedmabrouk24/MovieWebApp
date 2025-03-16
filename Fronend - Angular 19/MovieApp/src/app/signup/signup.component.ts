import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../service/auth/auth.service';
import { Router } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

@Component({
  selector: 'app-signup',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent {
  signupForm: FormGroup;
  errorMessage: string = '';
  successMessage: string = ''; 

  user = {
    userName: '',
    password: '',
    email: ''
  };
  
  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.signupForm = this.fb.group({
      userName: ['', [Validators.required]],
      email: ['', [Validators.required, this.emailValidator]],
      password: ['', [Validators.required]]
    });
  }

  emailValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
      const valid = emailRegex.test(control.value);
      return valid ? null : { invalidEmail: 'Please enter a valid email address' };
    };
  }

  onSignup(): void {
    if (this.signupForm.invalid) {
      return;
    }

    const { userName, email, password } = this.signupForm.value;
    console.log("user name is " + userName);
    this.authService.signup(userName, email, password).subscribe({
      next: (response) => {
        console.log(response);

        this.successMessage = 'User registered successfully!';
        this.errorMessage = ''; 

        setTimeout(() => {
          this.successMessage = ''; 
          this.router.navigate(['/login']);
        }, 5000);

        this.signupForm.reset(); 
      },
      error: (error) => {
        console.log("here is the error in signup : " + error.message);
        this.errorMessage = 'Signup failed. Please check your credentials.';
        this.successMessage = ''; 
      }
    });
  }
}
