import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap, catchError, of } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  LoginRequest,
  LoginResponse,
  SessionInfo,
} from '../models/login.model';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly baseUrl = `${environment.bffUrl}/auth`;

  private _isAuthenticated = signal(false);
  private _currentUsername = signal<string | null>(null);

  isAuthenticated = this._isAuthenticated.asReadonly();
  currentUsername = this._currentUsername.asReadonly();

  constructor(private http: HttpClient) {}

  login(request: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.baseUrl}/login`, request).pipe(
      tap((response: LoginResponse) => {
        if (response.success) {
          this._isAuthenticated.set(true);
          this._currentUsername.set(response.username ?? null);
        }
      })
    );
  }

  logout(): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/logout`, {}).pipe(
      tap(() => {
        this._isAuthenticated.set(false);
        this._currentUsername.set(null);
      }),
      catchError(() => {
        this._isAuthenticated.set(false);
        this._currentUsername.set(null);
        return of(undefined);
      })
    );
  }

  checkSession(): Observable<SessionInfo> {
    return this.http.get<SessionInfo>(`${this.baseUrl}/session`).pipe(
      tap((info: SessionInfo) => {
        this._isAuthenticated.set(info.authenticated);
        this._currentUsername.set(info.username ?? null);
      }),
      catchError(() => {
        this._isAuthenticated.set(false);
        this._currentUsername.set(null);
        return of({ authenticated: false } as SessionInfo);
      })
    );
  }
}
