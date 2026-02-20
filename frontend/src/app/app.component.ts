import { Component } from '@angular/core';
import { RouterOutlet, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from './core/services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, CommonModule],
  template: `
    <nav class="navbar" *ngIf="authService.isAuthenticated()">
      <div class="navbar-brand">
        <a routerLink="/products">TNB Microservices</a>
      </div>
      <div class="navbar-menu">
        <span class="navbar-user">{{ authService.currentUsername() }}</span>
        <button class="btn btn-logout" (click)="logout()">Çıkış Yap</button>
      </div>
    </nav>
    <main>
      <router-outlet></router-outlet>
    </main>
  `,
  styles: [
    `
      .navbar {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 0 2rem;
        height: 60px;
        background-color: #1a1a2e;
        color: white;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
      }
      .navbar-brand a {
        font-size: 1.25rem;
        font-weight: 700;
        color: #e94560;
      }
      .navbar-menu {
        display: flex;
        align-items: center;
        gap: 1rem;
      }
      .navbar-user {
        font-size: 0.9rem;
        color: #ccc;
      }
      .btn-logout {
        padding: 0.4rem 1rem;
        border: 1px solid #e94560;
        background: transparent;
        color: #e94560;
        border-radius: 4px;
        cursor: pointer;
        font-size: 0.85rem;
        transition: all 0.2s;
      }
      .btn-logout:hover {
        background: #e94560;
        color: white;
      }
      main {
        min-height: calc(100vh - 60px);
      }
    `,
  ],
})
export class AppComponent {
  constructor(public authService: AuthService) {}

  logout(): void {
    this.authService.logout().subscribe(() => {
      window.location.href = '/login';
    });
  }
}
