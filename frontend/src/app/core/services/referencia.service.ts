import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface Categoria { id: number; nombre: string; descripcion?: string; }
export interface Proveedor { id: number; nombre: string; nit?: string; }
export interface Especie { id: number; nombreComun: string; nombreCientifico?: string; }

@Injectable({ providedIn: 'root' })
export class ReferenciaService {
  private http = inject(HttpClient);
  private api = environment.apiUrl;

  categorias(): Observable<Categoria[]> { return this.http.get<Categoria[]>(`${this.api}/inventario/categorias`); }
  proveedores(): Observable<Proveedor[]> { return this.http.get<Proveedor[]>(`${this.api}/inventario/proveedores`); }
  especies(): Observable<Especie[]> { return this.http.get<Especie[]>(`${this.api}/produccion/especies`); }
}
