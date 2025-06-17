import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  Friend,
  FriendService,
  PendingFriendRequest,
} from '../../services/friend.service';
import { User, UserService } from '../../services/user.service';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-friends',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './friends.component.html',
})
export class FriendsComponent implements OnInit {
  private friendService = inject(FriendService);
  private userService = inject(UserService);
  private fb = inject(FormBuilder);

  friends = signal<Friend[]>([]);
  pendingRequests = signal<PendingFriendRequest[]>([]);
  users = signal<User[]>([]);
  isLoading = signal(true);
  error = signal<string | null>(null);
  success = signal<string | null>(null);
  inviteForm!: FormGroup;
  showInviteForm = signal(false);

  ngOnInit(): void {
    this.loadFriends();
    this.loadPendingRequests();
    this.initForm();
  }

  initForm(): void {
    this.inviteForm = this.fb.group({
      username: ['', Validators.required],
    });
  }

  loadFriends(): void {
    this.isLoading.set(true);
    this.error.set(null);

    this.friendService.getAllFriends().subscribe({
      next: (friends) => {
        this.friends.set(friends);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error loading friends', err);
        this.error.set('Failed to load friends');
        this.isLoading.set(false);
      },
    });
  }

  loadPendingRequests(): void {
    this.friendService.getPendingFriendRequests().subscribe({
      next: (requests) => {
        this.pendingRequests.set(requests);
      },
      error: (err) => {
        console.error('Error loading pending friend requests', err);
      },
    });
  }

  loadUsers(): void {
    this.userService.getAllUsers().subscribe({
      next: (users) => {
        this.users.set(users);
      },
      error: (err) => {
        console.error('Error loading users', err);
        this.error.set('Failed to load users');
      },
    });
  }

  toggleInviteForm(): void {
    if (!this.showInviteForm()) {
      this.loadUsers();
    }
    this.showInviteForm.update((value) => !value);
  }

  sendInvite(): void {
    if (this.inviteForm.valid) {
      const username = this.inviteForm.get('username')?.value;

      this.isLoading.set(true);
      this.error.set(null);
      this.success.set(null);

      this.friendService.sendFriendRequest(username).subscribe({
        next: () => {
          this.success.set(`Friend request sent to ${username}`);
          this.inviteForm.reset();
          this.isLoading.set(false);
          this.showInviteForm.set(false);
        },
        error: (err) => {
          console.error('Error sending friend request', err);
          this.error.set(err.error?.message || 'Failed to send friend request');
          this.isLoading.set(false);
        },
      });
    }
  }

  confirmRequest(requestId: number): void {
    this.isLoading.set(true);
    this.error.set(null);
    this.success.set(null);

    this.friendService.confirmFriendRequest(requestId).subscribe({
      next: () => {
        this.success.set('Friend request accepted');
        // Remove this request from pending requests
        this.pendingRequests.update((requests) =>
          requests.filter((request) => request.id !== requestId)
        );
        // Reload friends list to show the new friend
        this.loadFriends();
      },
      error: (err) => {
        console.error('Error confirming friend request', err);
        this.error.set(
          err.error?.message || 'Failed to confirm friend request'
        );
        this.isLoading.set(false);
      },
    });
  }

  rejectRequest(requestId: number): void {
    this.isLoading.set(true);
    this.error.set(null);
    this.success.set(null);

    this.friendService.rejectFriendRequest(requestId).subscribe({
      next: () => {
        this.success.set('Friend request rejected');
        // Remove this request from pending requests
        this.pendingRequests.update((requests) =>
          requests.filter((request) => request.id !== requestId)
        );
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error rejecting friend request', err);
        this.error.set(err.error?.message || 'Failed to reject friend request');
        this.isLoading.set(false);
      },
    });
  }

  viewFriendCollections(username: string): void {
    // This will be handled by the router
  }

  removeFriend(username: string): void {
    if (
      confirm(`Are you sure you want to remove ${username} from your friends?`)
    ) {
      this.isLoading.set(true);
      this.error.set(null);
      this.success.set(null);

      this.friendService.removeFriend(username).subscribe({
        next: () => {
          this.success.set(`Friend ${username} removed successfully`);
          // Update friends list to remove the friend
          this.friends.update((friends) =>
            friends.filter((friend) => friend.friendUsername !== username)
          );
          this.isLoading.set(false);
        },
        error: (err) => {
          console.error('Error removing friend', err);
          this.error.set(err.error?.message || 'Failed to remove friend');
          this.isLoading.set(false);
        },
      });
    }
  }
}
