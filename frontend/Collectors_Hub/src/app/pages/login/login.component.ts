import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService, LoginRequest } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.component.html',
})
export class LoginComponent {
  loginData: LoginRequest = {
    username: '',
    password: '',
  };

  isLoading = signal(false);
  errorMessage = signal('');
  successMessage = signal('');

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit() {
    if (!this.loginData.username || !this.loginData.password) {
      this.errorMessage.set('All fields are required');
      return;
    }

    this.isLoading.set(true);
    this.errorMessage.set('');
    this.successMessage.set('');

    this.authService.login(this.loginData).subscribe({
      next: (response) => {
        this.isLoading.set(false);
        this.successMessage.set(response.message);

        // Redirect to home page after successful login
        setTimeout(() => {
          this.router.navigate(['/']);
        }, 1000);
      },
      error: (error) => {
        this.isLoading.set(false);
        if (error.status === 401) {
          this.errorMessage.set('Invalid username or password');
        } else {
          this.errorMessage.set(
            'An error occurred during login. Please try again.'
          );
        }
      },
    });
  }
}
