import { Component, EventEmitter, Output, inject, signal } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import {
  CollectionService,
  CreateCollectionDto,
} from '../../services/collection.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-add-collection-modal',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './add-collection-modal.component.html',
})
export class AddCollectionModalComponent {
  @Output() close = new EventEmitter<void>();
  @Output() collectionCreated = new EventEmitter<void>();

  private fb = inject(FormBuilder);
  private collectionService = inject(CollectionService);

  collectionForm: FormGroup = this.fb.group({
    name: ['', Validators.required],
    description: ['', Validators.required],
  });

  isSubmitting = signal(false);

  onSubmit(): void {
    if (this.collectionForm.valid) {
      this.isSubmitting.set(true);
      const newCollection: CreateCollectionDto = this.collectionForm.value;
      this.collectionService.createCollection(newCollection).subscribe({
        next: () => {
          this.isSubmitting.set(false);
          this.collectionCreated.emit();
          this.close.emit();
        },
        error: (err) => {
          this.isSubmitting.set(false);
          console.error('Error creating collection', err);
          // Here you could add user-facing error handling
        },
      });
    }
  }
}
