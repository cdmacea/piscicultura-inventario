import { Component, inject } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet, Router } from '@angular/router';
import { AuthService } from '../core/services/auth.service';

@Component({
  selector: 'app-shell',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <div class="shell">
      <aside class="lateral">
        <div class="marca">
          <span class="pez">🐟</span>
          <div>
            <strong>Finca Piscícola</strong>
            <small>Inventario &amp; gestión</small>
          </div>
        </div>
        <nav>
          <a routerLink="/dashboard" routerLinkActive="activo"><span>◧</span> Panel</a>
          <a routerLink="/inventario" routerLinkActive="activo"><span>▦</span> Inventario</a>
          <a routerLink="/produccion" routerLinkActive="activo"><span>≋</span> Producción</a>
          <a routerLink="/negocio" routerLinkActive="activo"><span>₵</span> Negocio</a>
        </nav>
        <div class="pie">
          <div class="usuario">{{ auth.getUsername() }}</div>
          <button class="btn btn-fantasma salir" type="button" (click)="salir()">Cerrar sesión</button>
          <div class="version">v1.0 · PostgreSQL</div>
        </div>
      </aside>
      <main class="contenido">
        <router-outlet></router-outlet>
      </main>
    </div>
  `,
  styles: [`
    .shell { display: flex; min-height: 100vh; }
    .lateral {
      width: 248px;
      background: linear-gradient(180deg, var(--profundo) 0%, var(--profundo-2) 100%);
      color: #cfe6e3;
      display: flex;
      flex-direction: column;
      padding: 1.4rem 1rem;
      position: sticky;
      top: 0;
      height: 100vh;
    }
    .marca { display: flex; align-items: center; gap: .7rem; padding: 0 .4rem 1.4rem; }
    .marca .pez { font-size: 1.6rem; }
    .marca strong { display: block; color: #fff; font-family: 'Sora', sans-serif; font-size: 1rem; }
    .marca small { color: var(--agua-claro); font-size: .72rem; }
    nav { display: flex; flex-direction: column; gap: .25rem; }
    nav a {
      display: flex; align-items: center; gap: .7rem;
      color: #b7d6d2; padding: .65rem .8rem; border-radius: 10px;
      font-weight: 500; transition: background .15s, color .15s;
    }
    nav a span { width: 18px; text-align: center; opacity: .8; }
    nav a:hover { background: rgba(255,255,255,.06); color: #fff; }
    nav a.activo { background: var(--agua); color: #fff; }
    nav a.activo span { opacity: 1; }
    .pie { margin-top: auto; padding: .8rem .4rem 0; }
    .usuario { font-size: .78rem; color: #cfe6e3; margin-bottom: .5rem; }
    .salir { width: 100%; justify-content: center; border-color: rgba(255,255,255,.25); color: #cfe6e3; }
    .salir:hover { border-color: #fff; color: #fff; }
    .version { font-size: .72rem; color: #6f9a95; margin-top: .7rem; }
    .contenido { flex: 1; padding: 2rem 2.4rem; max-width: 1200px; }
    @media (max-width: 780px) {
      .shell { flex-direction: column; }
      .lateral { width: auto; height: auto; position: static; flex-direction: row; align-items: center; flex-wrap: wrap; }
      .lateral nav { flex-direction: row; flex-wrap: wrap; }
      .marca { padding-bottom: 0; }
      .pie { display: none; }
      .contenido { padding: 1.2rem; }
    }
  `]
})
export class ShellComponent {
  auth = inject(AuthService);
  private router = inject(Router);

  salir(): void {
    this.auth.logout();
    this.router.navigateByUrl('/login');
  }
}
