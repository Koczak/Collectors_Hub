import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { guestGuard, authGuard } from './guards/auth.guard';
import { CollectionsComponent } from './pages/collections/collections.component';
import { CollectionItemsComponent } from './pages/collection-items/collection-items.component';
import { CategoriesComponent } from './pages/categories/categories.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { FriendsComponent } from './pages/friends/friends.component';
import { FriendCollectionsComponent } from './pages/friend-collections/friend-collections.component';
import { FriendCollectionItemsComponent } from './pages/friend-collection-items/friend-collection-items.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [guestGuard], // Prevent access if already logged in
  },
  {
    path: 'register',
    component: RegisterComponent,
    canActivate: [guestGuard], // Prevent access if already logged in
  },
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [authGuard], // Require authentication
  },
  {
    path: 'collections',
    component: CollectionsComponent,
    canActivate: [authGuard], // Require authentication
  },
  {
    path: 'collections/:id/items',
    component: CollectionItemsComponent,
    canActivate: [authGuard], // Require authentication
  },
  {
    path: 'categories',
    component: CategoriesComponent,
    canActivate: [authGuard], // Require authentication
  },
  {
    path: 'profile',
    component: ProfileComponent,
    canActivate: [authGuard], // Require authentication
  },
  {
    path: 'friends',
    component: FriendsComponent,
    canActivate: [authGuard], // Require authentication
  },
  {
    path: 'friends/:username/collections',
    component: FriendCollectionsComponent,
    canActivate: [authGuard], // Require authentication
  },
  {
    path: 'friends/:username/collections/:collectionId/items',
    component: FriendCollectionItemsComponent,
    canActivate: [authGuard],
  },
  { path: '**', redirectTo: '' },
];
