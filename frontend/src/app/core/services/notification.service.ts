import { Injectable, signal } from '@angular/core';

export interface Notificacion {
  id: number;
  mensaje: string;
  tipo: 'error' | 'info';
}

let siguienteId = 1;

/** Cola simple de notificaciones (toasts) que consume ToastComponent. */
@Injectable({ providedIn: 'root' })
export class NotificationService {
  notificaciones = signal<Notificacion[]>([]);

  error(mensaje: string): void {
    this.mostrar(mensaje, 'error');
  }

  info(mensaje: string): void {
    this.mostrar(mensaje, 'info');
  }

  descartar(id: number): void {
    this.notificaciones.update(lista => lista.filter(n => n.id !== id));
  }

  private mostrar(mensaje: string, tipo: 'error' | 'info'): void {
    const id = siguienteId++;
    this.notificaciones.update(lista => [...lista, { id, mensaje, tipo }]);
    setTimeout(() => this.descartar(id), 6000);
  }
}
