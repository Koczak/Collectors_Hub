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
  FormArray,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import {
  Category,
  CategoryService,
  CreateCategoryDto,
} from '../../services/category.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-edit-category-modal',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './edit-category-modal.component.html',
})
export class EditCategoryModalComponent implements OnInit {
  @Input() category!: Category;
  @Output() close = new EventEmitter<void>();
  @Output() categoryUpdated = new EventEmitter<void>();

  private fb = inject(FormBuilder);
  private categoryService = inject(CategoryService);

  categoryForm!: FormGroup;
  isSubmitting = signal(false);

  ngOnInit(): void {
    this.initForm();
  }

  initForm(): void {
    this.categoryForm = this.fb.group({
      name: [this.category.name, Validators.required],
      attributes: this.fb.array([]),
    });

    // Initialize attributes if they exist
    if (this.category.attributes && this.category.attributes.length > 0) {
      this.category.attributes.forEach((attribute) => {
        this.attributesArray.push(
          this.fb.control(attribute, Validators.required)
        );
      });
    }
  }

  get attributesArray() {
    return this.categoryForm.get('attributes') as FormArray;
  }

  addAttribute() {
    this.attributesArray.push(this.fb.control('', Validators.required));
  }

  removeAttribute(index: number) {
    this.attributesArray.removeAt(index);
  }

  onSubmit(): void {
    if (this.categoryForm.valid) {
      this.isSubmitting.set(true);

      // Convert from FormArray to string array
      const attributes = this.attributesArray.controls.map(
        (control) => control.value
      );

      const updatedCategory: CreateCategoryDto = {
        name: this.categoryForm.get('name')?.value,
        attributes: attributes.length > 0 ? attributes : undefined,
      };

      this.categoryService
        .updateCategory(this.category.id, updatedCategory)
        .subscribe({
          next: () => {
            this.isSubmitting.set(false);
            this.categoryUpdated.emit();
          },
          error: (err) => {
            this.isSubmitting.set(false);
            console.error('Error updating category', err);
          },
        });
    }
  }
}
