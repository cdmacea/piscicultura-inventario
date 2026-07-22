import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Insumo, InsumoRequest } from '../models';

@Injectable({ providedIn: 'root' })
export class InsumoService {
  private http = inject(HttpClient);
  private base = `${environment.apiUrl}/inventario/insumos`;

  listar(): Observable<Insumo[]> { return this.http.get<Insumo[]>(this.base); }
  stockBajo(): Observable<Insumo[]> { return this.http.get<Insumo[]>(`${this.base}/stock-bajo`); }
  obtener(id: number): Observable<Insumo> { return this.http.get<Insumo>(`${this.base}/${id}`); }
  crear(req: InsumoRequest): Observable<Insumo> { return this.http.post<Insumo>(this.base, req); }
  actualizar(id: number, req: InsumoRequest): Observable<Insumo> { return this.http.put<Insumo>(`${this.base}/${id}`, req); }
  eliminar(id: number): Observable<void> { return this.http.delete<void>(`${this.base}/${id}`); }
}
