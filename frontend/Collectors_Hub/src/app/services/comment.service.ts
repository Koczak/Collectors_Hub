import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Comment {
  id: number;
  content: string;
  createdAt: string;
  username: string;
  userId: number;
}

export interface CommentDto {
  content: string;
  itemId: number;
}

@Injectable({
  providedIn: 'root',
})
export class CommentService {
  private apiUrl = `${environment.apiUrl}/comments`;

  constructor(private http: HttpClient) {}

  getCommentsForItem(itemId: number): Observable<Comment[]> {
    return this.http.get<Comment[]>(`${this.apiUrl}/item/${itemId}`, {
      withCredentials: true,
    });
  }

  getCommentsForCollection(collectionId: number): Observable<Comment[]> {
    return this.http.get<Comment[]>(
      `${this.apiUrl}/collection/${collectionId}`,
      {
        withCredentials: true,
      }
    );
  }

  addComment(comment: CommentDto): Observable<number> {
    return this.http.post<number>(this.apiUrl, comment, {
      withCredentials: true,
    });
  }

  deleteComment(commentId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${commentId}`, {
      withCredentials: true,
    });
  }
}
