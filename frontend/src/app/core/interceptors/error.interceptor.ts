import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { NotificationService } from '../services/notification.service';

/**
 * Red de seguridad global: si un componente no maneja el error de una
 * petición HTTP, igual queda visible aquí en lugar de fallar en silencio.
 */
export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const notify = inject(NotificationService);
  const auth = inject(AuthService);
  const router = inject(Router);

  return next(req).pipe(
    catchError((err: HttpErrorResponse) => {
      if (err.status === 401 && !req.url.includes('/auth/login')) {
        auth.logout();
        router.navigate(['/login']);
        notify.error('Tu sesión expiró. Inicia sesión de nuevo.');
      } else if (err.status === 0) {
        notify.error('No se pudo contactar al servidor. ¿Está activo el backend?');
      } else if (!req.url.includes('/auth/login')) {
        notify.error(err.error?.message ?? 'Ocurrió un error inesperado.');
      }
      return throwError(() => err);
    })
  );
};
