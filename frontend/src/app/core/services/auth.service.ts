import { HttpClient } from '@angular/common/http';
import { Injectable, inject, signal } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { LoginRequest, LoginResponse } from '../models';

const TOKEN_KEY = 'piscicultura.token';
const USERNAME_KEY = 'piscicultura.username';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private base = `${environment.apiUrl}/auth`;

  /** Señal reactiva para que la UI (ej. barra lateral) reaccione al login/logout. */
  autenticado = signal(this.getToken() !== null);

  login(req: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.base}/login`, req).pipe(
      tap(resp => {
        localStorage.setItem(TOKEN_KEY, resp.token);
        localStorage.setItem(USERNAME_KEY, resp.username);
        this.autenticado.set(true);
      })
    );
  }

  logout(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USERNAME_KEY);
    this.autenticado.set(false);
  }

  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  getUsername(): string | null {
    return localStorage.getItem(USERNAME_KEY);
  }

  isAuthenticated(): boolean {
    return this.getToken() !== null;
  }
}
