import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Movimiento, MovimientoRequest } from '../models';

@Injectable({ providedIn: 'root' })
export class MovimientoService {
  private http = inject(HttpClient);
  private base = `${environment.apiUrl}/inventario/movimientos`;

  recientes(): Observable<Movimiento[]> { return this.http.get<Movimiento[]>(this.base); }
  porInsumo(insumoId: number): Observable<Movimiento[]> { return this.http.get<Movimiento[]>(`${this.base}/insumo/${insumoId}`); }
  registrar(req: MovimientoRequest): Observable<Movimiento> { return this.http.post<Movimiento>(this.base, req); }
}
