import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Category {
  id: number;
  name: string;
  username?: string;
  attributes?: string[];
}

export interface CreateCategoryDto {
  name: string;
  attributes?: string[];
}

@Injectable({
  providedIn: 'root',
})
export class CategoryService {
  private apiUrl = `${environment.apiUrl}/categories`;

  constructor(private http: HttpClient) {}

  getAllCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(this.apiUrl, { withCredentials: true });
  }

  createCategory(categoryData: CreateCategoryDto): Observable<number> {
    return this.http.post<number>(this.apiUrl, categoryData, {
      withCredentials: true,
    });
  }

  updateCategory(
    id: number,
    categoryData: CreateCategoryDto
  ): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}`, categoryData, {
      withCredentials: true,
    });
  }

  deleteCategory(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, {
      withCredentials: true,
    });
  }
}
