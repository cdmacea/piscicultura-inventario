import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./features/login/login.component').then(m => m.LoginComponent),
    title: 'Iniciar sesión'
  },
  {
    path: '',
    loadComponent: () => import('./layout/shell.component').then(m => m.ShellComponent),
    canActivate: [authGuard],
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'dashboard' },
      {
        path: 'dashboard',
        loadComponent: () =>
          import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent),
        title: 'Panel'
      },
      {
        path: 'inventario',
        loadComponent: () =>
          import('./features/inventario/inventario.component').then(m => m.InventarioComponent),
        title: 'Inventario'
      },
      {
        path: 'produccion',
        loadComponent: () =>
          import('./features/produccion/produccion.component').then(m => m.ProduccionComponent),
        title: 'Producción'
      },
      {
        path: 'negocio',
        loadComponent: () =>
          import('./features/negocio/negocio.component').then(m => m.NegocioComponent),
        title: 'Negocio'
      }
    ]
  },
  { path: '**', redirectTo: 'dashboard' }
];
