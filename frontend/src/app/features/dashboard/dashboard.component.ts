import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { DashboardService } from '../../core/services/dashboard.service';
import { Resumen } from '../../core/models';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <header class="cab">
      <div>
        <h1>Panel de la finca</h1>
        <p class="muted">Vista general de inventario, producción y ventas.</p>
      </div>
    </header>

    <ng-container *ngIf="resumen as r; else cargando">
      <div class="grid-cards mt">
        <a class="metrica card" routerLink="/inventario">
          <span class="etq">Insumos activos</span>
          <span class="num">{{ r.totalInsumos }}</span>
        </a>
        <a class="metrica card" routerLink="/inventario"
           [class.alerta]="r.insumosStockBajo > 0">
          <span class="etq">Con stock bajo</span>
          <span class="num">{{ r.insumosStockBajo }}</span>
          <span class="pie-metrica" *ngIf="r.insumosStockBajo > 0">Requiere compra</span>
        </a>
        <a class="metrica card" routerLink="/produccion">
          <span class="etq">Lotes activos</span>
          <span class="num">{{ r.lotesActivos }}</span>
        </a>
        <a class="metrica card" routerLink="/produccion">
          <span class="etq">Peces en producción</span>
          <span class="num">{{ r.pecesEnProduccion | number }}</span>
        </a>
        <a class="metrica card destacada" routerLink="/negocio">
          <span class="etq">Ventas cobradas</span>
          <span class="num">{{ r.ventasPagadas | currency:'COP':'symbol-narrow':'1.0-0' }}</span>
        </a>
        <a class="metrica card" routerLink="/negocio">
          <span class="etq">Ventas pendientes</span>
          <span class="num">{{ r.ventasPendientes }}</span>
        </a>
      </div>
    </ng-container>

    <ng-template #cargando>
      <p class="vacio" *ngIf="!error">Cargando indicadores…</p>
      <p class="vacio" *ngIf="error">{{ error }}</p>
    </ng-template>
  `,
  styles: [`
    .cab { display: flex; justify-content: space-between; align-items: flex-start; }
    .metrica {
      display: flex; flex-direction: column; gap: .4rem;
      padding: 1.3rem 1.4rem; text-decoration: none; color: inherit;
      transition: transform .1s ease, box-shadow .15s ease;
    }
    .metrica:hover { transform: translateY(-2px); box-shadow: 0 10px 30px rgba(11,59,60,.1); }
    .etq { font-size: .78rem; color: var(--tinta-2); font-weight: 600; }
    .num { font-family: 'Sora', sans-serif; font-size: 2rem; font-weight: 700; color: var(--tinta); }
    .destacada { background: linear-gradient(135deg, var(--profundo), var(--profundo-2)); border: none; }
    .destacada .etq { color: var(--agua-claro); }
    .destacada .num { color: #fff; }
    .alerta { border-color: var(--coral); }
    .alerta .num { color: var(--coral); }
    .pie-metrica { font-size: .72rem; color: var(--coral); font-weight: 600; }
  `]
})
export class DashboardComponent implements OnInit {
  private service = inject(DashboardService);
  resumen?: Resumen;
  error = '';

  ngOnInit(): void {
    this.service.resumen().subscribe({
      next: r => this.resumen = r,
      error: () => this.error = 'No se pudo conectar con el servidor. Verifica que el backend esté activo en el puerto 8081.'
    });
  }
}
