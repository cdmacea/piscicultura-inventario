import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { authGuard } from './auth.guard';
import { AuthService } from '../services/auth.service';

describe('authGuard', () => {
  let authServiceStub: { isAuthenticated: jasmine.Spy };
  let routerSpy: { navigate: jasmine.Spy };

  beforeEach(() => {
    authServiceStub = { isAuthenticated: jasmine.createSpy('isAuthenticated') };
    routerSpy = { navigate: jasmine.createSpy('navigate') };

    TestBed.configureTestingModule({
      providers: [
        { provide: AuthService, useValue: authServiceStub },
        { provide: Router, useValue: routerSpy }
      ]
    });
  });

  function ejecutarGuard(): boolean {
    return TestBed.runInInjectionContext(() => authGuard({} as any, {} as any)) as boolean;
  }

  it('permite el acceso si hay sesión activa', () => {
    authServiceStub.isAuthenticated.and.returnValue(true);

    expect(ejecutarGuard()).toBeTrue();
    expect(routerSpy.navigate).not.toHaveBeenCalled();
  });

  it('bloquea y redirige a /login si no hay sesión', () => {
    authServiceStub.isAuthenticated.and.returnValue(false);

    expect(ejecutarGuard()).toBeFalse();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/login']);
  });
});
