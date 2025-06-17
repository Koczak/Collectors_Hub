import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import {
  CollectionService,
  Collection,
} from '../../services/collection.service';
import { ItemService, Item } from '../../services/item.service';
import { CategoryService, Category } from '../../services/category.service';
import {
  FriendService,
  Friend,
  PendingFriendRequest,
} from '../../services/friend.service';
import { UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-dashboard',
  // standalone: true,
  imports: [CommonModule, RouterLink],

  templateUrl: './dashboard.component.html',
})
export class DashboardComponent implements OnInit {
  collections: Collection[] = [];
  recentItems: Item[] = [];
  categories: Category[] = [];
  friends: Friend[] = [];
  pendingFriendRequests: PendingFriendRequest[] = [];
  username: string = '';
  isLoading: boolean = true;
  error: string | null = null;

  constructor(
    private collectionService: CollectionService,
    private itemService: ItemService,
    private categoryService: CategoryService,
    private friendService: FriendService,
    private userService: UserService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.username = this.authService.username();
    this.loadDashboardData();
  }

  loadDashboardData(): void {
    this.isLoading = true;

    // Load collections
    this.collectionService.getCollections().subscribe({
      next: (collections) => {
        this.collections = collections;
        this.loadItems();
      },
      error: (err) => {
        console.error('Error loading collections:', err);
        this.error = 'Failed to load collections';
        this.isLoading = false;
      },
    });

    // Load categories
    this.categoryService.getAllCategories().subscribe({
      next: (categories) => {
        this.categories = categories;
      },
      error: (err) => {
        console.error('Error loading categories:', err);
      },
    });

    // Load friends
    this.friendService.getAllFriends().subscribe({
      next: (friends) => {
        this.friends = friends;
      },
      error: (err) => {
        console.error('Error loading friends:', err);
      },
    });

    // Load pending friend requests
    this.friendService.getPendingFriendRequests().subscribe({
      next: (requests) => {
        this.pendingFriendRequests = requests;
      },
      error: (err) => {
        console.error('Error loading friend requests:', err);
      },
    });
  }

  loadItems(): void {
    this.itemService.getAllItems().subscribe({
      next: (items) => {
        // Get the most recent items (up to 5)
        this.recentItems = items.sort((a, b) => b.id - a.id).slice(0, 5);

        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error loading items:', err);
        this.error = 'Failed to load items';
        this.isLoading = false;
      },
    });
  }

  acceptFriendRequest(invitationId: number): void {
    this.friendService.confirmFriendRequest(invitationId).subscribe({
      next: () => {
        // Remove from pending list
        this.pendingFriendRequests = this.pendingFriendRequests.filter(
          (request) => request.id !== invitationId
        );
        // Reload friends
        this.friendService.getAllFriends().subscribe({
          next: (friends) => {
            this.friends = friends;
          },
        });
      },
      error: (err) => {
        console.error('Error accepting friend request:', err);
      },
    });
  }

  rejectFriendRequest(invitationId: number): void {
    this.friendService.rejectFriendRequest(invitationId).subscribe({
      next: () => {
        // Remove from pending list
        this.pendingFriendRequests = this.pendingFriendRequests.filter(
          (request) => request.id !== invitationId
        );
      },
      error: (err) => {
        console.error('Error rejecting friend request:', err);
      },
    });
  }
}
