import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Item {
  id: number;
  name: string;
  description: string;
  categoryName?: string;
  categoryId?: number;
  attributes?: Record<string, any>;
  collectionId: number;
}

export interface CreateItemDto {
  name: string;
  description: string;
  collectionId: number;
  categoryId?: number;
  attributes?: Record<string, any>;
}

@Injectable({
  providedIn: 'root',
})
export class ItemService {
  private apiUrl = `${environment.apiUrl}/items`;

  constructor(private http: HttpClient) {}

  getAllItems(): Observable<Item[]> {
    return this.http.get<Item[]>(this.apiUrl, { withCredentials: true });
  }

  getItemsByCollectionId(collectionId: number): Observable<Item[]> {
    // Use the collectionId field from the API response
    return this.getAllItems().pipe(
      map((items) => {
        return items.filter((item) => item.collectionId === collectionId);
      })
    );
  }

  createItem(itemData: CreateItemDto): Observable<number> {
    return this.http.post<number>(this.apiUrl, itemData, {
      withCredentials: true,
    });
  }

  updateItem(id: number, itemData: CreateItemDto): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}`, itemData, {
      withCredentials: true,
    });
  }

  deleteItem(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, {
      withCredentials: true,
    });
  }
}
