import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { PageResponse, Venta, VentaRequest } from '../models';

@Injectable({ providedIn: 'root' })
export class VentaService {
  private http = inject(HttpClient);
  private base = `${environment.apiUrl}/negocio/ventas`;

  listar(page = 0, size = 20): Observable<PageResponse<Venta>> {
    return this.http.get<PageResponse<Venta>>(this.base, { params: { page, size } });
  }
  crear(req: VentaRequest): Observable<Venta> { return this.http.post<Venta>(this.base, req); }
  cambiarEstado(id: number, estado: string): Observable<Venta> {
    return this.http.patch<Venta>(`${this.base}/${id}/estado?estado=${estado}`, {});
  }
}
