import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Collection } from './collection.service';
import { Item } from './item.service';

export interface Friend {
  friendUsername: string;
  friendEmail: string;
}

export interface PendingFriendRequest {
  id: number;
  senderUsername: string;
  senderEmail: string;
}

@Injectable({
  providedIn: 'root',
})
export class FriendService {
  private apiUrl = `${environment.apiUrl}/friends`;

  constructor(private http: HttpClient) {}

  getAllFriends(): Observable<Friend[]> {
    return this.http.get<Friend[]>(this.apiUrl, { withCredentials: true });
  }

  getPendingFriendRequests(): Observable<PendingFriendRequest[]> {
    return this.http.get<PendingFriendRequest[]>(`${this.apiUrl}/pending`, {
      withCredentials: true,
    });
  }

  sendFriendRequest(username: string): Observable<void> {
    return this.http.post<void>(
      `${this.apiUrl}/invite/${username}`,
      {},
      {
        withCredentials: true,
      }
    );
  }

  confirmFriendRequest(invitationId: number): Observable<void> {
    return this.http.get<void>(`${this.apiUrl}/confirm/${invitationId}`, {
      withCredentials: true,
    });
  }

  rejectFriendRequest(invitationId: number): Observable<void> {
    return this.http.post<void>(
      `${this.apiUrl}/reject/${invitationId}`,
      {},
      {
        withCredentials: true,
      }
    );
  }

  removeFriend(username: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${username}`, {
      withCredentials: true,
    });
  }

  getFriendCollections(username: string): Observable<Collection[]> {
    return this.http.get<Collection[]>(
      `${this.apiUrl}/collections/${username}`,
      {
        withCredentials: true,
      }
    );
  }

  getFriendCollectionItems(
    username: string,
    collectionId: number
  ): Observable<Item[]> {
    return this.http.get<Item[]>(
      `${this.apiUrl}/collections/${username}/${collectionId}/items`,
      {
        withCredentials: true,
      }
    );
  }
}
