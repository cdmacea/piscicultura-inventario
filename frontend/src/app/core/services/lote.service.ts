import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Lote, LoteRequest } from '../models';

@Injectable({ providedIn: 'root' })
export class LoteService {
  private http = inject(HttpClient);
  private base = `${environment.apiUrl}/produccion/lotes`;

  listar(): Observable<Lote[]> { return this.http.get<Lote[]>(this.base); }
  crear(req: LoteRequest): Observable<Lote> { return this.http.post<Lote>(this.base, req); }
  actualizar(id: number, req: LoteRequest): Observable<Lote> { return this.http.put<Lote>(`${this.base}/${id}`, req); }
  eliminar(id: number): Observable<void> { return this.http.delete<void>(`${this.base}/${id}`); }
}
