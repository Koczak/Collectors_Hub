import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Item, ItemService } from '../../services/item.service';
import {
  BehaviorSubject,
  Observable,
  catchError,
  combineLatest,
  map,
  of,
  switchMap,
} from 'rxjs';
import { CollectionService } from '../../services/collection.service';
import { AddItemModalComponent } from '../../components/add-item-modal/add-item-modal.component';
import { EditItemModalComponent } from '../../components/edit-item-modal/edit-item-modal.component';
import { Category, CategoryService } from '../../services/category.service';
import { FormsModule } from '@angular/forms';
import { ExportService } from '../../services/export.service';
import { CommentSectionComponent } from '../../components/comment-section/comment-section.component';
import { DeleteConfirmationModalComponent } from '../../components/delete-confirmation-modal/delete-confirmation-modal.component';

interface ItemFilters {
  nameSearch: string;
  categoryId: number | null;
}

interface ItemSorting {
  field: 'name' | 'category';
  direction: 'asc' | 'desc';
}

@Component({
  selector: 'app-collection-items',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    AddItemModalComponent,
    EditItemModalComponent,
    FormsModule,
    CommentSectionComponent,
    DeleteConfirmationModalComponent,
  ],
  templateUrl: './collection-items.component.html',
})
export class CollectionItemsComponent implements OnInit {
  collectionId: number = 0;
  collectionName: string = '';

  // Source data
  private itemsSubject = new BehaviorSubject<Item[]>([]);
  private filtersSubject = new BehaviorSubject<ItemFilters>({
    nameSearch: '',
    categoryId: null,
  });
  private sortingSubject = new BehaviorSubject<ItemSorting>({
    field: 'name',
    direction: 'asc',
  });

  // Public access to reactive data
  items$: Observable<Item[]> = this.itemsSubject.asObservable();
  filteredItems$: Observable<Item[]>;
  categories: Category[] = [];

  // UI state
  filters: ItemFilters = {
    nameSearch: '',
    categoryId: null,
  };

  sorting: ItemSorting = {
    field: 'name',
    direction: 'asc',
  };

  isAddModalOpen = signal(false);
  isEditModalOpen = signal(false);
  isDeleteModalOpen = signal(false);
  selectedItem = signal<Item | null>(null);
  showExportOptions = signal(false);
  itemToDeleteId = signal<number | null>(null);

  // Helper method to iterate over object keys in the template
  objectKeys = Object.keys;

  constructor(
    private route: ActivatedRoute,
    private itemService: ItemService,
    private collectionService: CollectionService,
    private categoryService: CategoryService,
    private exportService: ExportService
  ) {
    // Set up the filtered items observable
    this.filteredItems$ = combineLatest([
      this.items$,
      this.filtersSubject.asObservable(),
      this.sortingSubject.asObservable(),
    ]).pipe(
      map(([items, filters, sorting]) => {
        // Apply filters
        let result = [...items];

        // Filter by name
        if (filters.nameSearch) {
          const search = filters.nameSearch.toLowerCase();
          result = result.filter(
            (item) =>
              item.name.toLowerCase().includes(search) ||
              (item.description &&
                item.description.toLowerCase().includes(search))
          );
        }

        // Filter by category
        if (filters.categoryId !== null) {
          result = result.filter(
            (item) => item.categoryId === filters.categoryId
          );
        }

        // Apply sorting
        result.sort((a, b) => {
          let comparison = 0;

          if (sorting.field === 'name') {
            comparison = a.name.localeCompare(b.name);
          } else if (sorting.field === 'category') {
            const catA = a.categoryName || '';
            const catB = b.categoryName || '';
            comparison = catA.localeCompare(catB);
          }

          return sorting.direction === 'asc' ? comparison : -comparison;
        });

        return result;
      })
    );
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      const id = params.get('id');
      if (id) {
        this.collectionId = +id;
        this.loadCollectionDetails();
        this.loadItems();
        this.loadCategories();
      }
    });
  }

  loadCollectionDetails(): void {
    this.collectionService.getCollections().subscribe((collections) => {
      const collection = collections.find((c) => c.id === this.collectionId);
      if (collection) {
        this.collectionName = collection.name;
      }
    });
  }

  loadItems(): void {
    this.itemService
      .getItemsByCollectionId(this.collectionId)
      .pipe(
        catchError((error) => {
          console.error('Error loading items', error);
          return of([]);
        })
      )
      .subscribe((items) => {
        this.itemsSubject.next(items);
      });
  }

  loadCategories(): void {
    this.categoryService.getAllCategories().subscribe((categories) => {
      this.categories = categories;
    });
  }

  applyFilters(): void {
    this.filtersSubject.next({ ...this.filters });
  }

  applySorting(): void {
    this.sortingSubject.next({ ...this.sorting });
  }

  resetFiltersAndSorting(): void {
    this.filters = {
      nameSearch: '',
      categoryId: null,
    };
    this.sorting = {
      field: 'name',
      direction: 'asc',
    };
    this.applyFilters();
    this.applySorting();
  }

  exportItems(): void {
    // Get current filtered items
    this.filteredItems$
      .subscribe((items) => {
        if (items.length === 0) {
          alert('No items to export');
          return;
        }

        const fileName = `${this.collectionName.replace(
          /\s+/g,
          '_'
        )}_items.csv`;
        this.exportService.exportToCsv(items, fileName);
      })
      .unsubscribe();
  }

  exportItemsToExcel(): void {
    // Get current filtered items
    this.filteredItems$
      .subscribe((items) => {
        if (items.length === 0) {
          alert('No items to export');
          return;
        }

        const fileName = `${this.collectionName.replace(
          /\s+/g,
          '_'
        )}_items.xlsx`;
        this.exportService.exportToExcel(items, fileName);
      })
      .unsubscribe();
  }

  exportItemsToPdf(): void {
    // Get current filtered items
    this.filteredItems$
      .subscribe((items) => {
        if (items.length === 0) {
          alert('No items to export');
          return;
        }

        const fileName = `${this.collectionName.replace(
          /\s+/g,
          '_'
        )}_items.pdf`;
        this.exportService.exportToPdf(items, fileName);
      })
      .unsubscribe();
  }

  toggleExportOptions(): void {
    this.showExportOptions.update((value) => !value);
  }

  openAddModal(): void {
    this.isAddModalOpen.set(true);
  }

  closeAddModal(): void {
    this.isAddModalOpen.set(false);
  }

  openEditModal(item: Item): void {
    this.selectedItem.set(item);
    this.isEditModalOpen.set(true);
  }

  closeEditModal(): void {
    this.isEditModalOpen.set(false);
    this.selectedItem.set(null);
  }

  openDeleteModal(item: Item): void {
    this.selectedItem.set(item);
    this.itemToDeleteId.set(item.id);
    this.isDeleteModalOpen.set(true);
  }

  closeDeleteModal(): void {
    this.isDeleteModalOpen.set(false);
    this.selectedItem.set(null);
    this.itemToDeleteId.set(null);
  }

  onItemCreated(): void {
    this.loadItems();
    this.closeAddModal();
  }

  onItemUpdated(): void {
    this.loadItems();
    this.closeEditModal();
  }

  onDeleteItem(id: number): void {
    this.openDeleteModal(
      this.itemsSubject.getValue().find((item) => item.id === id)!
    );
  }

  confirmDelete(): void {
    if (this.itemToDeleteId()) {
      this.itemService.deleteItem(this.itemToDeleteId()!).subscribe({
        next: () => {
          this.loadItems();
          this.closeDeleteModal();
        },
        error: (error) => {
          console.error('Error deleting item', error);
          this.closeDeleteModal();
        },
      });
    }
  }
}
