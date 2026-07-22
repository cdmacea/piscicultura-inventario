import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ClienteService } from '../../core/services/cliente.service';
import { VentaService } from '../../core/services/venta.service';
import { Cliente, ClienteRequest, Venta, VentaRequest } from '../../core/models';

@Component({
  selector: 'app-negocio',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './negocio.component.html',
  styles: [`
    .cab { margin-bottom: 1.2rem; }
    .panel { padding: 1.3rem 1.4rem; }
    .form-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(150px, 1fr)); gap: .8rem; }
    .acciones { display: flex; gap: .5rem; justify-content: flex-end; margin-top: 1rem; }
    .titulo-sec { display: flex; justify-content: space-between; align-items: center; margin: 1.8rem 0 .8rem; }
    .mono { font-variant-numeric: tabular-nums; }
    .linea { display: grid; grid-template-columns: 2fr 1fr 1fr auto; gap: .6rem; align-items: end; margin-bottom: .6rem; }
    .total { text-align: right; font-family: 'Sora', sans-serif; font-weight: 700; font-size: 1.1rem; margin-top: .6rem; }
    .msg { padding: .6rem .9rem; border-radius: 10px; margin-bottom: 1rem; font-size: .85rem; }
    .msg-error { background: #fdecea; color: #c0392b; }
    .msg-ok { background: #e2f4ec; color: var(--exito); }
    .paginador { padding: .9rem 1rem; border-top: 1px solid var(--linea); font-size: .85rem; }
  `]
})
export class NegocioComponent implements OnInit {
  private clienteSvc = inject(ClienteService);
  private ventaSvc = inject(VentaService);

  clientes: Cliente[] = [];
  ventas: Venta[] = [];
  paginaVentas = 0;
  totalPaginasVentas = 0;

  mostrarFormCliente = false;
  mostrarFormVenta = false;
  mensaje = ''; error = '';

  tiposDoc = ['CC', 'NIT', 'CE', 'PAS'];
  metodosPago = ['EFECTIVO', 'TRANSFERENCIA', 'CREDITO'];

  nuevoCliente: ClienteRequest = this.clienteVacio();
  nuevaVenta: VentaRequest = this.ventaVacia();

  ngOnInit(): void { this.cargar(); }

  cargar(): void {
    this.clienteSvc.listar().subscribe({
      next: d => this.clientes = d,
      error: () => this.error = 'No se pudo cargar el módulo de negocio. ¿Está activo el backend?'
    });
    this.cargarVentas();
  }

  cargarVentas(): void {
    this.ventaSvc.listar(this.paginaVentas).subscribe(pag => {
      this.ventas = pag.content;
      this.totalPaginasVentas = pag.totalPages;
    });
  }

  paginaAnterior(): void {
    if (this.paginaVentas > 0) { this.paginaVentas--; this.cargarVentas(); }
  }

  paginaSiguiente(): void {
    if (this.paginaVentas < this.totalPaginasVentas - 1) { this.paginaVentas++; this.cargarVentas(); }
  }

  crearCliente(): void {
    this.limpiar();
    this.clienteSvc.crear(this.nuevoCliente).subscribe({
      next: () => { this.mensaje = 'Cliente creado.'; this.nuevoCliente = this.clienteVacio(); this.mostrarFormCliente = false; this.cargar(); },
      error: e => this.error = e?.error?.message ?? 'No se pudo crear el cliente.'
    });
  }

  agregarLinea(): void {
    this.nuevaVenta.detalles.push({ descripcion: '', cantidadKg: 0, precioKg: 0 });
  }

  quitarLinea(i: number): void {
    this.nuevaVenta.detalles.splice(i, 1);
  }

  get totalVenta(): number {
    return this.nuevaVenta.detalles.reduce((s, d) => s + (d.cantidadKg || 0) * (d.precioKg || 0), 0);
  }

  crearVenta(): void {
    this.limpiar();
    if (!this.nuevaVenta.clienteId) { this.error = 'Selecciona un cliente.'; return; }
    if (this.nuevaVenta.detalles.length === 0) { this.error = 'Agrega al menos una línea de venta.'; return; }
    this.ventaSvc.crear(this.nuevaVenta).subscribe({
      next: () => { this.mensaje = 'Venta registrada.'; this.nuevaVenta = this.ventaVacia(); this.mostrarFormVenta = false; this.cargar(); },
      error: e => this.error = e?.error?.message ?? 'No se pudo registrar la venta.'
    });
  }

  marcarPagada(v: Venta): void {
    this.ventaSvc.cambiarEstado(v.id, 'PAGADA').subscribe(() => this.cargar());
  }

  private clienteVacio(): ClienteRequest {
    return { nombre: '', tipoDocumento: 'CC', documento: '', telefono: '', email: '', direccion: '', activo: true };
  }

  private ventaVacia(): VentaRequest {
    return { codigo: '', clienteId: 0, fecha: new Date().toISOString().slice(0, 10),
             metodoPago: 'EFECTIVO', detalles: [{ descripcion: '', cantidadKg: 0, precioKg: 0 }] };
  }

  private limpiar(): void { this.mensaje = ''; this.error = ''; }
}
