import { Component, EventEmitter, Output, inject, signal } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormArray,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import {
  CategoryService,
  CreateCategoryDto,
} from '../../services/category.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-add-category-modal',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './add-category-modal.component.html',
})
export class AddCategoryModalComponent {
  @Output() close = new EventEmitter<void>();
  @Output() categoryCreated = new EventEmitter<void>();

  private fb = inject(FormBuilder);
  private categoryService = inject(CategoryService);

  categoryForm: FormGroup = this.fb.group({
    name: ['', Validators.required],
    attributes: this.fb.array([]),
  });

  isSubmitting = signal(false);

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

      const newCategory: CreateCategoryDto = {
        name: this.categoryForm.get('name')?.value,
        attributes: attributes.length > 0 ? attributes : undefined,
      };

      this.categoryService.createCategory(newCategory).subscribe({
        next: () => {
          this.isSubmitting.set(false);
          this.categoryCreated.emit();
        },
        error: (err) => {
          this.isSubmitting.set(false);
          console.error('Error creating category', err);
        },
      });
    }
  }
}
