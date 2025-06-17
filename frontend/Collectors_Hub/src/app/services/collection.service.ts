import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Collection {
  id: number;
  name: string;
  description: string;
  itemsCount: number;
}

export interface CreateCollectionDto {
  name: string;
  description: string;
}

@Injectable({
  providedIn: 'root',
})
export class CollectionService {
  private apiUrl = `${environment.apiUrl}/collections`;

  constructor(private http: HttpClient) {}

  getCollections(): Observable<Collection[]> {
    console.log('getCollections');
    return this.http.get<Collection[]>(this.apiUrl, { withCredentials: true });
  }

  createCollection(
    collectionData: CreateCollectionDto
  ): Observable<Collection> {
    return this.http.post<Collection>(this.apiUrl, collectionData, {
      withCredentials: true,
    });
  }
}
