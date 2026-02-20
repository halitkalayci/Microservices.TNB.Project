import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () =>
      import('./features/login/login.component').then((m) => m.LoginComponent),
  },
  {
    path: 'products',
    loadComponent: () =>
      import('./features/products/product-list.component').then(
        (m) => m.ProductListComponent
      ),
    canActivate: [authGuard],
  },
  {
    path: '',
    redirectTo: 'products',
    pathMatch: 'full',
  },
  {
    path: '**',
    redirectTo: 'login',
  },
];
