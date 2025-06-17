import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Collection } from '../../services/collection.service';
import { FriendService } from '../../services/friend.service';

@Component({
  selector: 'app-friend-collections',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './friend-collections.component.html',
})
export class FriendCollectionsComponent implements OnInit {
  private friendService = inject(FriendService);
  private route = inject(ActivatedRoute);

  friendUsername = signal<string>('');
  collections = signal<Collection[]>([]);
  isLoading = signal(true);
  error = signal<string | null>(null);

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      const username = params.get('username');
      if (username) {
        this.friendUsername.set(username);
        this.loadFriendCollections(username);
      }
    });
  }

  loadFriendCollections(username: string): void {
    this.isLoading.set(true);
    this.error.set(null);

    this.friendService.getFriendCollections(username).subscribe({
      next: (collections) => {
        this.collections.set(collections);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error loading friend collections', err);
        this.error.set('Failed to load collections');
        this.isLoading.set(false);
      },
    });
  }
}
