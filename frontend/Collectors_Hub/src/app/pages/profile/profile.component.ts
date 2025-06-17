import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { User, UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './profile.component.html',
})
export class ProfileComponent implements OnInit {
  private userService = inject(UserService);
  public authService = inject(AuthService);

  user = signal<User | null>(null);
  isLoading = signal(true);
  error = signal<string | null>(null);

  ngOnInit(): void {
    this.loadUserProfile();
  }

  loadUserProfile(): void {
    this.isLoading.set(true);
    this.error.set(null);

    this.userService.getUserProfile().subscribe({
      next: (user) => {
        this.user.set(user);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error loading user profile', err);
        this.error.set('Failed to load user profile');
        this.isLoading.set(false);
      },
    });
  }

  // Helper method to safely get the first letter of username
  getUserInitial(): string {
    const currentUser = this.user();
    if (
      currentUser &&
      currentUser.username &&
      currentUser.username.length > 0
    ) {
      return currentUser.username.charAt(0).toUpperCase();
    }
    return '?';
  }
}
