import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { map, take } from 'rxjs/operators';
import { of } from 'rxjs';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // Ensure auth status is checked
  return authService.checkAuthStatus().pipe(
    map((status) => {
      if (authService.isAuthenticated()) {
        return true;
      } else {
        return router.createUrlTree(['/login']);
      }
    }),
    take(1) // Ensure the observable completes
  );
};

export const guestGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // Ensure auth status is checked
  return authService.checkAuthStatus().pipe(
    map((status) => {
      if (!authService.isAuthenticated()) {
        return true;
      } else {
        return router.createUrlTree(['/']);
      }
    }),
    take(1) // Ensure the observable completes
  );
};
