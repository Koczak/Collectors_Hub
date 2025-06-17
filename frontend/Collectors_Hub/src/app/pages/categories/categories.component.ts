import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Category, CategoryService } from '../../services/category.service';
import { Observable } from 'rxjs';
import { RouterModule } from '@angular/router';
import { AddCategoryModalComponent } from '../../components/add-category-modal/add-category-modal.component';
import { EditCategoryModalComponent } from '../../components/edit-category-modal/edit-category-modal.component';

@Component({
  selector: 'app-categories',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    AddCategoryModalComponent,
    EditCategoryModalComponent,
  ],
  templateUrl: './categories.component.html',
})
export class CategoriesComponent implements OnInit {
  categories$: Observable<Category[]> | undefined;
  isAddModalOpen = signal(false);
  isEditModalOpen = signal(false);
  selectedCategory = signal<Category | null>(null);

  constructor(private categoryService: CategoryService) {}

  ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories(): void {
    this.categories$ = this.categoryService.getAllCategories();
  }

  openAddModal(): void {
    this.isAddModalOpen.set(true);
  }

  closeAddModal(): void {
    this.isAddModalOpen.set(false);
  }

  openEditModal(category: Category): void {
    this.selectedCategory.set(category);
    this.isEditModalOpen.set(true);
  }

  closeEditModal(): void {
    this.isEditModalOpen.set(false);
    this.selectedCategory.set(null);
  }

  onCategoryCreated(): void {
    this.loadCategories();
    this.closeAddModal();
  }

  onCategoryUpdated(): void {
    this.loadCategories();
    this.closeEditModal();
  }

  deleteCategory(id: number): void {
    if (
      confirm(
        'Are you sure you want to delete this category? Items using this category will no longer be categorized.'
      )
    ) {
      this.categoryService.deleteCategory(id).subscribe({
        next: () => {
          this.loadCategories();
        },
        error: (err) => {
          console.error('Error deleting category', err);
        },
      });
    }
  }
}
