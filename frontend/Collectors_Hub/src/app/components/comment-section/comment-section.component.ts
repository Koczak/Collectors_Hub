import { Component, Input, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  Comment,
  CommentDto,
  CommentService,
} from '../../services/comment.service';
import { AuthService } from '../../services/auth.service';

interface User {
  id: number;
  username: string;
}

@Component({
  selector: 'app-comment-section',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="bg-white overflow-hidden sm:rounded-lg border border-gray-200">
      <div
        class="px-4 py-3 border-b border-gray-200 sm:px-6 flex justify-between items-center"
      >
        <h3 class="text-sm font-medium text-gray-900">{{ title }}</h3>
        <span class="text-xs text-gray-500"
          >{{ comments().length }}
          {{ comments().length === 1 ? 'comment' : 'comments' }}</span
        >
      </div>

      <!-- Comments list -->
      <div class="bg-white overflow-hidden max-h-60 overflow-y-auto">
        <ul class="divide-y divide-gray-200">
          @if (isLoading()) {
          <li class="px-4 py-2 text-center">
            <div
              class="animate-spin rounded-full h-5 w-5 border-b-2 border-indigo-500 mx-auto"
            ></div>
          </li>
          } @else if (comments().length === 0) {
          <li class="px-4 py-2 text-center text-gray-500 text-xs">
            No comments yet.
          </li>
          } @else { @for (comment of comments(); track comment.id) {
          <li class="px-4 py-2">
            <div class="flex justify-between">
              <div class="flex-1">
                <div class="flex items-center justify-between">
                  <span class="text-xs font-medium text-indigo-600">
                    {{ comment.username }}
                  </span>
                  <span class="text-xs text-gray-400">
                    {{ formatDate(comment.createdAt) }}
                  </span>
                </div>
                <div class="mt-1 text-sm text-gray-900">
                  {{ comment.content }}
                </div>
              </div>
              @if (isCommentAuthor(comment)) {
              <button
                (click)="deleteComment(comment.id)"
                class="ml-2 text-red-600 hover:text-red-900 self-start"
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  class="h-4 w-4"
                  viewBox="0 0 20 20"
                  fill="currentColor"
                >
                  <path
                    fill-rule="evenodd"
                    d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z"
                    clip-rule="evenodd"
                  />
                </svg>
              </button>
              }
            </div>
          </li>
          } }
        </ul>
      </div>

      <!-- Add comment form -->
      @if (authService.isAuthenticated()) {
      <div class="px-4 py-2 border-t border-gray-200">
        <div class="flex">
          <input
            type="text"
            [(ngModel)]="newComment"
            placeholder="Add a comment..."
            class="shadow-sm focus:ring-indigo-500 focus:border-indigo-500 block w-full text-sm border-gray-300 rounded-md mr-2"
          />
          <button
            (click)="addComment()"
            [disabled]="!newComment.trim()"
            class="inline-flex items-center px-2 py-1 border border-transparent text-xs font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
          >
            Post
          </button>
        </div>
      </div>
      } @else {
      <div
        class="px-4 py-2 border-t border-gray-200 text-center text-gray-500 text-xs"
      >
        <p>Please log in to add comments.</p>
      </div>
      }
    </div>
  `,
})
export class CommentSectionComponent implements OnInit {
  @Input() itemId!: number;
  @Input() title: string = 'Comments';

  comments = signal<Comment[]>([]);
  isLoading = signal<boolean>(true);
  newComment = '';
  currentUsername: string = '';

  constructor(
    private commentService: CommentService,
    public authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadComments();
    if (this.authService.isAuthenticated()) {
      this.currentUsername = this.authService.username();
    }
  }

  loadComments(): void {
    this.isLoading.set(true);
    this.commentService.getCommentsForItem(this.itemId).subscribe({
      next: (comments) => {
        this.comments.set(comments);
        this.isLoading.set(false);
      },
      error: (error) => {
        console.error('Error loading comments', error);
        this.isLoading.set(false);
      },
    });
  }

  addComment(): void {
    if (!this.newComment.trim()) return;

    const commentDto: CommentDto = {
      content: this.newComment.trim(),
      itemId: this.itemId,
    };

    this.commentService.addComment(commentDto).subscribe({
      next: () => {
        this.newComment = '';
        this.loadComments();
      },
      error: (error) => {
        console.error('Error adding comment', error);
      },
    });
  }

  deleteComment(commentId: number): void {
    this.commentService.deleteComment(commentId).subscribe({
      next: () => {
        this.loadComments();
      },
      error: (error) => {
        console.error('Error deleting comment', error);
      },
    });
  }

  isCommentAuthor(comment: Comment): boolean {
    return this.currentUsername === comment.username;
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleString(undefined, {
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  }
}
