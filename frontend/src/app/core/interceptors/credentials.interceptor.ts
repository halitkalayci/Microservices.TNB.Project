import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

/**
 * Adds withCredentials: true to all HTTP requests so the browser
 * sends the HttpOnly session cookie to the BFF.
 * Also handles 401 responses by redirecting to login.
 */
export const credentialsInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);

  const clonedReq = req.clone({
    withCredentials: true,
  });

  return next(clonedReq).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401 && !req.url.includes('/auth/session')) {
        router.navigate(['/login']);
      }
      return throwError(() => error);
    })
  );
};
