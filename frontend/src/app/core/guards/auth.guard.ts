import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { map } from 'rxjs';
import { AuthService } from '../services/auth.service';

/**
 * Route guard that checks if the user has a valid session via the BFF.
 * Redirects to /login if not authenticated.
 */
export const authGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.checkSession().pipe(
    map((session) => {
      if (session.authenticated) {
        return true;
      }
      router.navigate(['/login']);
      return false;
    })
  );
};
