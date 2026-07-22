// ---- Inventario ----
export interface Insumo {
  id: number;
  codigo: string;
  nombre: string;
  categoriaId: number;
  categoriaNombre: string;
  proveedorId?: number;
  proveedorNombre?: string;
  unidadMedida: string;
  stockActual: number;
  stockMinimo: number;
  precioUnitario: number;
  stockBajo: boolean;
  activo: boolean;
}

export interface InsumoRequest {
  codigo: string;
  nombre: string;
  categoriaId: number;
  proveedorId?: number | null;
  unidadMedida: string;
  stockMinimo: number;
  precioUnitario: number;
  stockInicial?: number;
  activo?: boolean;
}

export type TipoMovimiento = 'ENTRADA' | 'SALIDA' | 'AJUSTE';

export interface Movimiento {
  id: number;
  insumoId: number;
  insumoNombre: string;
  tipo: TipoMovimiento;
  cantidad: number;
  stockResultante: number;
  motivo?: string;
  usuario?: string;
  fecha: string;
}

export interface MovimientoRequest {
  insumoId: number;
  tipo: TipoMovimiento;
  cantidad: number;
  motivo?: string;
  usuario?: string;
}

// ---- Producción ----
export interface Estanque {
  id: number;
  codigo: string;
  nombre: string;
  tipo: string;
  areaM2?: number;
  volumenM3?: number;
  capacidadMax?: number;
  estado: string;
}
export type EstanqueRequest = Omit<Estanque, 'id'>;

export interface Lote {
  id: number;
  codigo: string;
  especieId: number;
  especieNombre: string;
  estanqueId: number;
  estanqueNombre: string;
  fechaSiembra: string;
  cantidadInicial: number;
  cantidadActual: number;
  pesoPromedioG: number;
  estado: string;
  observaciones?: string;
}
export interface LoteRequest {
  codigo: string;
  especieId: number;
  estanqueId: number;
  fechaSiembra: string;
  cantidadInicial: number;
  cantidadActual?: number;
  pesoPromedioG?: number;
  estado?: string;
  observaciones?: string;
}

// ---- Negocio ----
export interface Cliente {
  id: number;
  nombre: string;
  tipoDocumento: string;
  documento?: string;
  telefono?: string;
  email?: string;
  direccion?: string;
  activo: boolean;
}
export type ClienteRequest = Omit<Cliente, 'id'>;

export interface DetalleVenta {
  id?: number;
  loteId?: number | null;
  descripcion: string;
  cantidadKg: number;
  precioKg: number;
  subtotal?: number;
}
export interface Venta {
  id: number;
  codigo: string;
  clienteId: number;
  clienteNombre: string;
  fecha: string;
  total: number;
  estado: string;
  metodoPago: string;
  detalles: DetalleVenta[];
}
export interface VentaRequest {
  codigo: string;
  clienteId: number;
  fecha?: string;
  metodoPago?: string;
  detalles: DetalleVenta[];
}

// ---- Dashboard ----
export interface Resumen {
  totalInsumos: number;
  insumosStockBajo: number;
  lotesActivos: number;
  pecesEnProduccion: number;
  ventasPagadas: number;
  ventasPendientes: number;
}

// ---- Paginación (Spring Data Page) ----
export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

// ---- Autenticación ----
export interface LoginRequest {
  username: string;
  password: string;
}
export interface LoginResponse {
  token: string;
  username: string;
}
