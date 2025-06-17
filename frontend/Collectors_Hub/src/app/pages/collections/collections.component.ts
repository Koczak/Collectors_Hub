import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  Collection,
  CollectionService,
} from '../../services/collection.service';
import { Observable } from 'rxjs';
import { RouterLink } from '@angular/router';
import { AddCollectionModalComponent } from '../../components/add-collection-modal/add-collection-modal.component';

@Component({
  selector: 'app-collections',
  standalone: true,
  imports: [CommonModule, RouterLink, AddCollectionModalComponent],
  templateUrl: './collections.component.html',
})
export class CollectionsComponent implements OnInit {
  collections$: Observable<Collection[]> | undefined;
  isModalOpen = signal(false);

  constructor(private collectionService: CollectionService) {}

  ngOnInit(): void {
    this.loadCollections();
  }

  loadCollections(): void {
    this.collections$ = this.collectionService.getCollections();
  }

  openModal(): void {
    this.isModalOpen.set(true);
  }

  closeModal(): void {
    this.isModalOpen.set(false);
  }

  onCollectionCreated(): void {
    this.loadCollections();
    this.closeModal();
  }
}
