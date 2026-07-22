import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Estanque, EstanqueRequest } from '../models';

@Injectable({ providedIn: 'root' })
export class EstanqueService {
  private http = inject(HttpClient);
  private base = `${environment.apiUrl}/produccion/estanques`;

  listar(): Observable<Estanque[]> { return this.http.get<Estanque[]>(this.base); }
  crear(req: EstanqueRequest): Observable<Estanque> { return this.http.post<Estanque>(this.base, req); }
  actualizar(id: number, req: EstanqueRequest): Observable<Estanque> { return this.http.put<Estanque>(`${this.base}/${id}`, req); }
  eliminar(id: number): Observable<void> { return this.http.delete<void>(`${this.base}/${id}`); }
}
