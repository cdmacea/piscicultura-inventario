import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EstanqueService } from '../../core/services/estanque.service';
import { LoteService } from '../../core/services/lote.service';
import { Especie, ReferenciaService } from '../../core/services/referencia.service';
import { Estanque, EstanqueRequest, Lote, LoteRequest } from '../../core/models';

@Component({
  selector: 'app-produccion',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './produccion.component.html',
  styles: [`
    .cab { margin-bottom: 1.2rem; }
    .panel { padding: 1.3rem 1.4rem; }
    .form-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(150px, 1fr)); gap: .8rem; }
    .acciones { display: flex; gap: .5rem; justify-content: flex-end; margin-top: 1rem; }
    .titulo-sec { display: flex; justify-content: space-between; align-items: center; margin: 1.8rem 0 .8rem; }
    .mono { font-variant-numeric: tabular-nums; }
    .msg { padding: .6rem .9rem; border-radius: 10px; margin-bottom: 1rem; font-size: .85rem; }
    .msg-error { background: #fdecea; color: #c0392b; }
    .msg-ok { background: #e2f4ec; color: var(--exito); }
  `]
})
export class ProduccionComponent implements OnInit {
  private estanqueSvc = inject(EstanqueService);
  private loteSvc = inject(LoteService);
  private refSvc = inject(ReferenciaService);

  estanques: Estanque[] = [];
  lotes: Lote[] = [];
  especies: Especie[] = [];

  mostrarFormEstanque = false;
  mostrarFormLote = false;
  mensaje = ''; error = '';

  tiposEstanque = ['TIERRA', 'GEOMEMBRANA', 'CONCRETO', 'JAULA'];
  estadosEstanque = ['ACTIVO', 'MANTENIMIENTO', 'INACTIVO'];
  estadosLote = ['ACTIVO', 'COSECHADO', 'PERDIDO'];

  nuevoEstanque: EstanqueRequest = this.estanqueVacio();
  nuevoLote: LoteRequest = this.loteVacio();

  ngOnInit(): void {
    this.cargar();
    this.refSvc.especies().subscribe(e => this.especies = e);
  }

  cargar(): void {
    this.estanqueSvc.listar().subscribe({
      next: d => this.estanques = d,
      error: () => this.error = 'No se pudo cargar producción. ¿Está activo el backend?'
    });
    this.loteSvc.listar().subscribe(d => this.lotes = d);
  }

  crearEstanque(): void {
    this.limpiar();
    this.estanqueSvc.crear(this.nuevoEstanque).subscribe({
      next: () => { this.mensaje = 'Estanque creado.'; this.nuevoEstanque = this.estanqueVacio(); this.mostrarFormEstanque = false; this.cargar(); },
      error: e => this.error = e?.error?.message ?? 'No se pudo crear el estanque.'
    });
  }

  crearLote(): void {
    this.limpiar();
    this.loteSvc.crear(this.nuevoLote).subscribe({
      next: () => { this.mensaje = 'Lote sembrado.'; this.nuevoLote = this.loteVacio(); this.mostrarFormLote = false; this.cargar(); },
      error: e => this.error = e?.error?.message ?? 'No se pudo crear el lote.'
    });
  }

  eliminarEstanque(e: Estanque): void {
    if (!confirm(`¿Eliminar el estanque "${e.nombre}"?`)) return;
    this.estanqueSvc.eliminar(e.id).subscribe(() => this.cargar());
  }

  eliminarLote(l: Lote): void {
    if (!confirm(`¿Eliminar el lote "${l.codigo}"?`)) return;
    this.loteSvc.eliminar(l.id).subscribe(() => this.cargar());
  }

  private estanqueVacio(): EstanqueRequest {
    return { codigo: '', nombre: '', tipo: 'TIERRA', estado: 'ACTIVO',
             areaM2: undefined, volumenM3: undefined, capacidadMax: undefined };
  }

  private loteVacio(): LoteRequest {
    return { codigo: '', especieId: 0, estanqueId: 0, fechaSiembra: new Date().toISOString().slice(0, 10),
             cantidadInicial: 0, pesoPromedioG: 0, estado: 'ACTIVO', observaciones: '' };
  }

  private limpiar(): void { this.mensaje = ''; this.error = ''; }
}
