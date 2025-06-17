import {
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output,
  inject,
  signal,
} from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormControl,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import { CreateItemDto, Item, ItemService } from '../../services/item.service';
import { CommonModule } from '@angular/common';
import { Category, CategoryService } from '../../services/category.service';
import { RouterLink, RouterModule } from '@angular/router';

@Component({
  selector: 'app-edit-item-modal',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, RouterModule],
  templateUrl: './edit-item-modal.component.html',
})
export class EditItemModalComponent implements OnInit {
  @Input() item!: Item;
  @Input() collectionId!: number;
  @Output() close = new EventEmitter<void>();
  @Output() itemUpdated = new EventEmitter<void>();

  private fb = inject(FormBuilder);
  private itemService = inject(ItemService);
  private categoryService = inject(CategoryService);

  itemForm!: FormGroup;
  categories: Category[] = [];
  selectedCategoryAttributes: string[] = [];

  isSubmitting = signal(false);
  isLoading = signal(true);
  errorMessage = signal<string | null>(null);

  ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories(): void {
    this.isLoading.set(true);
    this.categoryService.getAllCategories().subscribe({
      next: (categories) => {
        this.categories = categories;
        this.initForm();
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error loading categories', err);
        this.isLoading.set(false);
        this.errorMessage.set('Error loading categories');
      },
    });
  }

  initForm(): void {
    // Najpierw znajdźmy kategorię na podstawie nazwy kategorii z przedmiotu
    let selectedCategory: Category | undefined;

    // Debug - wyświetl item
    console.log('Item to edit:', this.item);

    if (this.item.categoryName) {
      selectedCategory = this.categories.find(
        (c) => c.name === this.item.categoryName
      );
      console.log('Found category by name:', selectedCategory);
    }

    // Jeśli mamy categoryId, użyjmy go
    if (this.item.categoryId) {
      const categoryIdNum =
        typeof this.item.categoryId === 'string'
          ? parseInt(this.item.categoryId as any, 10)
          : this.item.categoryId;

      selectedCategory = this.categories.find((c) => c.id === categoryIdNum);
      console.log('Found category by ID:', selectedCategory);
    }

    // Tworzymy formularz
    this.itemForm = this.fb.group({
      name: [this.item.name, Validators.required],
      description: [this.item.description, Validators.required],
      categoryId: [selectedCategory ? selectedCategory.id : -1],
      attributes: this.fb.group({}),
    });

    // Jeśli mamy wybraną kategorię, inicjalizujemy atrybuty
    if (selectedCategory) {
      this.setupCategoryAttributes(selectedCategory.id, this.item.attributes);
    }

    // Nasłuchujemy zmian kategorii
    this.itemForm.get('categoryId')?.valueChanges.subscribe((newCategoryId) => {
      // Ensure categoryId is a number
      const categoryIdNum =
        typeof newCategoryId === 'string'
          ? parseInt(newCategoryId, 10)
          : newCategoryId;
      this.onCategoryChange(categoryIdNum);
    });
  }

  setupCategoryAttributes(
    categoryId: number,
    existingAttributes?: Record<string, any>
  ): void {
    const selectedCategory = this.categories.find((c) => c.id === categoryId);
    if (selectedCategory && selectedCategory.attributes) {
      this.selectedCategoryAttributes = selectedCategory.attributes;

      // Get attributes control group
      const attributesGroup = this.itemForm.get('attributes') as FormGroup;

      // Add form controls for each category attribute
      selectedCategory.attributes.forEach((attr) => {
        // Wartość z istniejących atrybutów lub pusta wartość
        const value =
          existingAttributes && attr in existingAttributes
            ? existingAttributes[attr]
            : '';

        attributesGroup.addControl(attr, new FormControl(value));
      });

      console.log('Initialized attributes form group:', attributesGroup.value);
    }
  }

  onCategoryChange(categoryId: number): void {
    // Clear existing attributes
    const attributesGroup = this.itemForm.get('attributes') as FormGroup;
    Object.keys(attributesGroup.controls).forEach((key) => {
      attributesGroup.removeControl(key);
    });

    this.selectedCategoryAttributes = [];

    // If a valid category is selected, set up attributes
    if (categoryId && categoryId !== -1) {
      this.setupCategoryAttributes(categoryId);
    }
  }

  onSubmit(): void {
    if (this.itemForm.valid) {
      this.isSubmitting.set(true);
      this.errorMessage.set(null);

      // Extract form values
      const { name, description } = this.itemForm.value;
      // Ensure categoryId is a number
      let categoryId = this.itemForm.value.categoryId;
      if (typeof categoryId === 'string') {
        categoryId = parseInt(categoryId, 10);
      }

      const attributesGroup = this.itemForm.get('attributes') as FormGroup;

      // Prepare attributes object
      const attributes: Record<string, any> = {};
      if (
        categoryId !== -1 &&
        Object.keys(attributesGroup.controls).length > 0
      ) {
        Object.keys(attributesGroup.controls).forEach((key) => {
          attributes[key] = attributesGroup.get(key)?.value;
        });
      }

      // Create the DTO - ensure empty attributes object for category items
      const updatedItem: CreateItemDto = {
        name,
        description,
        collectionId: this.collectionId,
        categoryId: categoryId !== -1 ? categoryId : undefined,
        // Important: If category is set, ensure we send an empty object for attributes if no attributes are set
        attributes:
          categoryId !== -1
            ? Object.keys(attributes).length > 0
              ? attributes
              : {}
            : undefined,
      };

      console.log('Sending update:', updatedItem);

      this.itemService.updateItem(this.item.id, updatedItem).subscribe({
        next: () => {
          this.isSubmitting.set(false);
          this.itemUpdated.emit();
        },
        error: (err) => {
          this.isSubmitting.set(false);
          this.errorMessage.set(err.error?.message || 'Error updating item');
          console.error('Error updating item', err);
        },
      });
    }
  }
}
