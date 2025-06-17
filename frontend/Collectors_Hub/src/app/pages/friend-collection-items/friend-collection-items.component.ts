import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Item } from '../../services/item.service';
import {
  BehaviorSubject,
  Observable,
  catchError,
  combineLatest,
  map,
  of,
} from 'rxjs';
import { FriendService } from '../../services/friend.service';
import { CollectionService } from '../../services/collection.service';
import { FormsModule } from '@angular/forms';
import { ExportService } from '../../services/export.service';
import { CategoryService } from '../../services/category.service';
import { CommentSectionComponent } from '../../components/comment-section/comment-section.component';

interface ItemFilters {
  nameSearch: string;
  categoryId: number | null;
}

interface ItemSorting {
  field: 'name' | 'category';
  direction: 'asc' | 'desc';
}

@Component({
  selector: 'app-friend-collection-items',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule, CommentSectionComponent],
  templateUrl: './friend-collection-items.component.html',
})
export class FriendCollectionItemsComponent implements OnInit {
  collectionId = signal<number>(0);
  collectionName = signal<string>('');
  friendUsername = signal<string>('');
  isLoading = signal<boolean>(true);
  showExportOptions = signal(false);

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
  items$ = this.itemsSubject.asObservable();
  filteredItems$: Observable<Item[]>;
  categories: any[] = [];

  // UI state
  filters: ItemFilters = {
    nameSearch: '',
    categoryId: null,
  };

  sorting: ItemSorting = {
    field: 'name',
    direction: 'asc',
  };

  // Helper method to iterate over object keys in the template
  objectKeys = Object.keys;

  constructor(
    private route: ActivatedRoute,
    private friendService: FriendService,
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
      const id = params.get('collectionId');
      const username = params.get('username');
      if (id && username) {
        this.collectionId.set(+id);
        this.friendUsername.set(username);
        this.loadFriendCollection();
        this.loadFriendCollectionItems();
        this.loadCategories();
      }
    });
  }

  loadFriendCollection(): void {
    this.friendService
      .getFriendCollections(this.friendUsername())
      .subscribe((collections) => {
        const collection = collections.find(
          (c) => c.id === this.collectionId()
        );
        if (collection) {
          this.collectionName.set(collection.name);
        }
      });
  }

  loadFriendCollectionItems(): void {
    this.isLoading.set(true);
    this.friendService
      .getFriendCollectionItems(this.friendUsername(), this.collectionId())
      .pipe(
        catchError((error) => {
          console.error('Error loading friend collection items', error);
          return of([]);
        })
      )
      .subscribe({
        next: (items) => {
          this.itemsSubject.next(items);
          this.isLoading.set(false);
        },
        error: () => {
          this.isLoading.set(false);
        },
      });
  }

  loadCategories(): void {
    this.categoryService.getAllCategories().subscribe({
      next: (categories) => {
        this.categories = categories;
      },
      error: (error) => {
        console.error('Error loading categories', error);
      },
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

        const fileName = `${this.friendUsername()}_${this.collectionName().replace(
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

        const fileName = `${this.friendUsername()}_${this.collectionName().replace(
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

        const fileName = `${this.friendUsername()}_${this.collectionName().replace(
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
}
