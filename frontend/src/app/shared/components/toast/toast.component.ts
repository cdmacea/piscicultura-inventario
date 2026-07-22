import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificationService } from '../../../core/services/notification.service';

@Component({
  selector: 'app-toast',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="pila">
      @for (n of notify.notificaciones(); track n.id) {
        <div class="toast" [class.toast-error]="n.tipo === 'error'" (click)="notify.descartar(n.id)">
          {{ n.mensaje }}
        </div>
      }
    </div>
  `,
  styles: [`
    .pila {
      position: fixed; top: 1rem; right: 1rem; z-index: 1000;
      display: flex; flex-direction: column; gap: .5rem; max-width: 320px;
    }
    .toast {
      background: var(--tinta); color: #fff; padding: .7rem 1rem;
      border-radius: 10px; font-size: .85rem; box-shadow: var(--sombra);
      cursor: pointer;
    }
    .toast-error { background: #c0392b; }
  `]
})
export class ToastComponent {
  notify = inject(NotificationService);
}
