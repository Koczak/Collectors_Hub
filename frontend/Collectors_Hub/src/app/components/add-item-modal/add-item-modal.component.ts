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
import { CreateItemDto, ItemService } from '../../services/item.service';
import { CommonModule } from '@angular/common';
import { Category, CategoryService } from '../../services/category.service';
import { RouterLink, RouterModule } from '@angular/router';

@Component({
  selector: 'app-add-item-modal',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, RouterModule],
  templateUrl: './add-item-modal.component.html',
})
export class AddItemModalComponent implements OnInit {
  @Input() collectionId!: number;
  @Output() close = new EventEmitter<void>();
  @Output() itemCreated = new EventEmitter<void>();

  private fb = inject(FormBuilder);
  private itemService = inject(ItemService);
  private categoryService = inject(CategoryService);

  categories: Category[] = [];
  selectedCategoryAttributes: string[] = [];

  itemForm: FormGroup = this.fb.group({
    name: ['', Validators.required],
    description: ['', Validators.required],
    categoryId: [-1], // Default to no category
    attributes: this.fb.group({}),
  });

  isSubmitting = signal(false);
  isLoading = signal(true);

  ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories(): void {
    this.isLoading.set(true);
    this.categoryService.getAllCategories().subscribe({
      next: (categories) => {
        this.categories = categories;
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error loading categories', err);
        this.isLoading.set(false);
      },
    });
  }

  onCategoryChange(): void {
    let categoryId = this.itemForm.get('categoryId')?.value;

    // Ensure categoryId is a number
    if (typeof categoryId === 'string') {
      categoryId = parseInt(categoryId, 10);
    }

    // Clear existing attributes
    const attributesGroup = this.itemForm.get('attributes') as FormGroup;
    Object.keys(attributesGroup.controls).forEach((key) => {
      attributesGroup.removeControl(key);
    });

    this.selectedCategoryAttributes = [];

    // If a valid category is selected, set up attributes
    if (categoryId && categoryId !== -1) {
      const selectedCategory = this.categories.find((c) => c.id === categoryId);
      if (selectedCategory && selectedCategory.attributes) {
        this.selectedCategoryAttributes = selectedCategory.attributes;

        // Create form controls for each attribute
        selectedCategory.attributes.forEach((attr) => {
          attributesGroup.addControl(attr, new FormControl(''));
        });
      }
    }
  }

  onSubmit(): void {
    if (this.itemForm.valid) {
      this.isSubmitting.set(true);

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

      // Create the DTO
      const newItem: CreateItemDto = {
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

      console.log('Creating item:', newItem);

      this.itemService.createItem(newItem).subscribe({
        next: () => {
          this.isSubmitting.set(false);
          this.itemCreated.emit();
        },
        error: (err) => {
          this.isSubmitting.set(false);
          console.error('Error creating item', err);
        },
      });
    }
  }
}
