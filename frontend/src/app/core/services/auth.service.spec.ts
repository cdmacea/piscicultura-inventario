import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { AuthService } from './auth.service';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    localStorage.clear();
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()]
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('empieza sin sesión si no hay token guardado', () => {
    expect(service.isAuthenticated()).toBeFalse();
  });

  it('login guarda el token y marca la sesión como autenticada', () => {
    service.login({ username: 'admin', password: 'changeme-local-dev' }).subscribe();

    const req = httpMock.expectOne(r => r.url.endsWith('/auth/login'));
    expect(req.request.method).toBe('POST');
    req.flush({ token: 'un-jwt-de-prueba', username: 'admin' });

    expect(service.getToken()).toBe('un-jwt-de-prueba');
    expect(service.getUsername()).toBe('admin');
    expect(service.isAuthenticated()).toBeTrue();
    expect(service.autenticado()).toBeTrue();
  });

  it('logout borra el token y la sesión', () => {
    localStorage.setItem('piscicultura.token', 'algo');
    service.logout();

    expect(service.getToken()).toBeNull();
    expect(service.isAuthenticated()).toBeFalse();
  });
});
