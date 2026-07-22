import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { InsumoService } from './insumo.service';
import { environment } from '../../../environments/environment';
import { Insumo } from '../models';

describe('InsumoService', () => {
  let service: InsumoService;
  let httpMock: HttpTestingController;
  const base = `${environment.apiUrl}/inventario/insumos`;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()]
    });
    service = TestBed.inject(InsumoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('listar() hace GET a /inventario/insumos', () => {
    const mock: Insumo[] = [];
    service.listar().subscribe(res => expect(res).toBe(mock));

    const req = httpMock.expectOne(base);
    expect(req.request.method).toBe('GET');
    req.flush(mock);
  });

  it('stockBajo() hace GET a /inventario/insumos/stock-bajo', () => {
    service.stockBajo().subscribe();

    const req = httpMock.expectOne(`${base}/stock-bajo`);
    expect(req.request.method).toBe('GET');
    req.flush([]);
  });

  it('eliminar() hace DELETE al insumo indicado', () => {
    service.eliminar(7).subscribe();

    const req = httpMock.expectOne(`${base}/7`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });
});
