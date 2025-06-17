import { Injectable, signal, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, catchError } from 'rxjs';
import { tap } from 'rxjs/operators';
import { environment } from '../../environments/environment';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  roles: string;
}

export interface AuthStatus {
  isAuthenticated: boolean;
  username: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = environment.apiUrl;
  private http = inject(HttpClient);

  // Signal for authentication status
  public isAuthenticated = signal<boolean>(false);
  public username = signal<string>('');
  public isLoading = signal<boolean>(true);

  login(credentials: LoginRequest): Observable<{ message: string }> {
    const formData = new FormData();
    formData.append('username', credentials.username);
    formData.append('password', credentials.password);

    return this.http
      .post<{ message: string }>(`${this.apiUrl}/auth/login`, formData, {
        withCredentials: true,
      })
      .pipe(
        tap(() => {
          // Update auth status after successful login
          this.checkAuthStatus().subscribe();
        })
      );
  }

  register(userData: RegisterRequest): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/users`, userData, {
      withCredentials: true,
    });
  }

  logout(): Observable<{ message: string }> {
    return this.http
      .post<{ message: string }>(
        `${this.apiUrl}/auth/logout`,
        {},
        {
          withCredentials: true,
        }
      )
      .pipe(
        tap(() => {
          this.isAuthenticated.set(false);
          this.username.set('');
        })
      );
  }
  checkAuthStatus(): Observable<AuthStatus> {
    this.isLoading.set(true);

    return this.http
      .get<AuthStatus>(`${this.apiUrl}/auth/status`, {
        withCredentials: true,
      })
      .pipe(
        tap((status) => {
          this.isAuthenticated.set(status.isAuthenticated);
          this.username.set(status.username);
          this.isLoading.set(false);
        }),
        catchError((error) => {
          console.error('AuthService: Authentication check failed:', error);
          this.isAuthenticated.set(false);
          this.username.set('');
          this.isLoading.set(false);
          return of({ isAuthenticated: false, username: '' });
        })
      );
  }
}
