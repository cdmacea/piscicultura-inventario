import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="fondo">
      <form class="card panel" (ngSubmit)="entrar()">
        <div class="marca">
          <span class="pez">🐟</span>
          <div>
            <strong>Finca Piscícola</strong>
            <small>Inventario &amp; gestión</small>
          </div>
        </div>

        <label for="username">Usuario</label>
        <input id="username" name="username" [(ngModel)]="username" autocomplete="username" required />

        <label for="password">Contraseña</label>
        <input id="password" name="password" type="password" [(ngModel)]="password"
               autocomplete="current-password" required />

        @if (error) {
          <div class="msg msg-error">{{ error }}</div>
        }

        <button class="btn btn-primario" type="submit" [disabled]="cargando">
          {{ cargando ? 'Ingresando…' : 'Ingresar' }}
        </button>
      </form>
    </div>
  `,
  styles: [`
    .fondo {
      min-height: 100vh;
      display: flex; align-items: center; justify-content: center;
      background: linear-gradient(180deg, var(--profundo) 0%, var(--profundo-2) 100%);
      padding: 1rem;
    }
    .panel { width: 100%; max-width: 340px; padding: 2rem 1.8rem; display: flex; flex-direction: column; gap: .3rem; }
    .marca { display: flex; align-items: center; gap: .7rem; margin-bottom: 1.4rem; }
    .marca .pez { font-size: 1.8rem; }
    .marca strong { display: block; font-family: 'Sora', sans-serif; }
    .marca small { color: var(--tinta-2); font-size: .78rem; }
    label { margin-top: .8rem; }
    .btn { width: 100%; justify-content: center; margin-top: 1.4rem; }
    .msg { padding: .6rem .9rem; border-radius: 10px; margin-top: 1rem; font-size: .85rem; }
    .msg-error { background: #fdecea; color: #c0392b; }
  `]
})
export class LoginComponent {
  private auth = inject(AuthService);
  private router = inject(Router);

  username = '';
  password = '';
  error = '';
  cargando = false;

  entrar(): void {
    this.error = '';
    if (!this.username || !this.password) {
      this.error = 'Ingresa usuario y contraseña.';
      return;
    }
    this.cargando = true;
    this.auth.login({ username: this.username, password: this.password }).subscribe({
      next: () => this.router.navigateByUrl('/dashboard'),
      error: e => {
        this.cargando = false;
        this.error = e?.error?.message ?? 'Usuario o contraseña incorrectos.';
      }
    });
  }
}
