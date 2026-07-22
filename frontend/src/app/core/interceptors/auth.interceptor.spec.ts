import { TestBed } from '@angular/core/testing';
import { HttpClient, provideHttpClient, withInterceptors } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { authInterceptor } from './auth.interceptor';
import { AuthService } from '../services/auth.service';

describe('authInterceptor', () => {
  let http: HttpClient;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    localStorage.clear();
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(withInterceptors([authInterceptor])),
        provideHttpClientTesting()
      ]
    });
    http = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('agrega el header Authorization cuando hay un token guardado', () => {
    TestBed.inject(AuthService); // asegura que el servicio use el mismo localStorage
    localStorage.setItem('piscicultura.token', 'un-jwt');

    http.get('/api/inventario/insumos').subscribe();

    const req = httpMock.expectOne('/api/inventario/insumos');
    expect(req.request.headers.get('Authorization')).toBe('Bearer un-jwt');
    req.flush([]);
  });

  it('no agrega el header si no hay sesión', () => {
    http.get('/api/inventario/insumos').subscribe();

    const req = httpMock.expectOne('/api/inventario/insumos');
    expect(req.request.headers.has('Authorization')).toBeFalse();
    req.flush([]);
  });

  it('no agrega el header en la petición de login, aunque haya un token viejo', () => {
    localStorage.setItem('piscicultura.token', 'un-jwt-viejo');

    http.post('/api/auth/login', { username: 'admin', password: 'x' }).subscribe();

    const req = httpMock.expectOne('/api/auth/login');
    expect(req.request.headers.has('Authorization')).toBeFalse();
    req.flush({ token: 'nuevo', username: 'admin' });
  });
});
