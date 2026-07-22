import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InsumoService } from '../../core/services/insumo.service';
import { MovimientoService } from '../../core/services/movimiento.service';
import { Categoria, Proveedor, ReferenciaService } from '../../core/services/referencia.service';
import { Insumo, InsumoRequest, Movimiento, MovimientoRequest, TipoMovimiento } from '../../core/models';

@Component({
  selector: 'app-inventario',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './inventario.component.html',
  styles: [`
    .cab { display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.2rem; }
    .panel { padding: 1.3rem 1.4rem; }
    .form-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(150px, 1fr)); gap: .8rem; }
    .acciones { display: flex; gap: .5rem; justify-content: flex-end; margin-top: 1rem; }
    .mono { font-variant-numeric: tabular-nums; }
    .titulo-sec { margin: 1.8rem 0 .8rem; }
    .msg { padding: .6rem .9rem; border-radius: 10px; margin-bottom: 1rem; font-size: .85rem; }
    .msg-error { background: #fdecea; color: #c0392b; }
    .msg-ok { background: #e2f4ec; color: var(--exito); }
  `]
})
export class InventarioComponent implements OnInit {
  private insumoSvc = inject(InsumoService);
  private movSvc = inject(MovimientoService);
  private refSvc = inject(ReferenciaService);

  insumos: Insumo[] = [];
  movimientos: Movimiento[] = [];
  categorias: Categoria[] = [];
  proveedores: Proveedor[] = [];

  mostrarFormInsumo = false;
  mensaje = '';
  error = '';

  nuevoInsumo: InsumoRequest = this.insumoVacio();
  mov: MovimientoRequest = { insumoId: 0, tipo: 'ENTRADA', cantidad: 0 };
  tipos: TipoMovimiento[] = ['ENTRADA', 'SALIDA', 'AJUSTE'];

  ngOnInit(): void {
    this.cargar();
    this.refSvc.categorias().subscribe(c => this.categorias = c);
    this.refSvc.proveedores().subscribe(p => this.proveedores = p);
  }

  cargar(): void {
    this.insumoSvc.listar().subscribe({
      next: d => this.insumos = d,
      error: () => this.error = 'No se pudo cargar el inventario. ¿Está activo el backend?'
    });
    this.movSvc.recientes().subscribe(m => this.movimientos = m);
  }

  crearInsumo(): void {
    this.limpiarMensajes();
    this.insumoSvc.crear(this.nuevoInsumo).subscribe({
      next: () => {
        this.mensaje = 'Insumo creado correctamente.';
        this.nuevoInsumo = this.insumoVacio();
        this.mostrarFormInsumo = false;
        this.cargar();
      },
      error: e => this.error = e?.error?.message ?? 'No se pudo crear el insumo.'
    });
  }

  registrarMovimiento(): void {
    this.limpiarMensajes();
    if (!this.mov.insumoId || this.mov.cantidad <= 0) {
      this.error = 'Selecciona un insumo e indica una cantidad mayor a cero.';
      return;
    }
    this.movSvc.registrar(this.mov).subscribe({
      next: () => {
        this.mensaje = 'Movimiento registrado. Stock actualizado.';
        this.mov = { insumoId: 0, tipo: 'ENTRADA', cantidad: 0 };
        this.cargar();
      },
      error: e => this.error = e?.error?.message ?? 'No se pudo registrar el movimiento.'
    });
  }

  eliminar(i: Insumo): void {
    if (!confirm(`¿Dar de baja el insumo "${i.nombre}"?`)) return;
    this.insumoSvc.eliminar(i.id).subscribe(() => this.cargar());
  }

  private insumoVacio(): InsumoRequest {
    return {
      codigo: '', nombre: '', categoriaId: 0, proveedorId: null,
      unidadMedida: 'kg', stockMinimo: 0, precioUnitario: 0, stockInicial: 0, activo: true
    };
  }

  private limpiarMensajes(): void { this.mensaje = ''; this.error = ''; }
}
