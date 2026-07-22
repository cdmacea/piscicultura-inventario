-- ============================================================================
--  ÁREA 3: NEGOCIO
--  Ejecutar CONECTADO a la base "piscicultura" (después de 02_...).
-- ============================================================================

CREATE SCHEMA IF NOT EXISTS negocio;
COMMENT ON SCHEMA negocio IS 'Clientes, ventas y gastos';

-- ---------------------------------------------------------------------------
-- Clientes
-- ---------------------------------------------------------------------------
CREATE TABLE negocio.cliente (
    id             BIGSERIAL PRIMARY KEY,
    nombre         VARCHAR(150) NOT NULL,
    tipo_documento VARCHAR(10)  NOT NULL DEFAULT 'CC'
                   CHECK (tipo_documento IN ('CC','NIT','CE','PAS')),
    documento      VARCHAR(30)  UNIQUE,
    telefono       VARCHAR(30),
    email          VARCHAR(120),
    direccion      VARCHAR(200),
    activo         BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at     TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- ---------------------------------------------------------------------------
-- Ventas
-- ---------------------------------------------------------------------------
CREATE TABLE negocio.venta (
    id          BIGSERIAL PRIMARY KEY,
    codigo      VARCHAR(40)  NOT NULL UNIQUE,
    cliente_id  BIGINT       NOT NULL REFERENCES negocio.cliente (id),
    fecha       DATE         NOT NULL DEFAULT CURRENT_DATE,
    total       NUMERIC(14,2) NOT NULL DEFAULT 0,
    estado      VARCHAR(20)  NOT NULL DEFAULT 'PENDIENTE'
                CHECK (estado IN ('PENDIENTE','PAGADA','ANULADA')),
    metodo_pago VARCHAR(20)  DEFAULT 'EFECTIVO'
                CHECK (metodo_pago IN ('EFECTIVO','TRANSFERENCIA','CREDITO')),
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_venta_cliente ON negocio.venta (cliente_id);
CREATE INDEX idx_venta_fecha   ON negocio.venta (fecha);

-- ---------------------------------------------------------------------------
-- Detalle de venta (líneas). Puede referenciar un lote de producción.
-- ---------------------------------------------------------------------------
CREATE TABLE negocio.detalle_venta (
    id           BIGSERIAL PRIMARY KEY,
    venta_id     BIGINT       NOT NULL REFERENCES negocio.venta (id) ON DELETE CASCADE,
    lote_id      BIGINT       REFERENCES produccion.lote (id),
    descripcion  VARCHAR(150) NOT NULL,
    cantidad_kg  NUMERIC(12,3) NOT NULL CHECK (cantidad_kg > 0),
    precio_kg    NUMERIC(14,2) NOT NULL CHECK (precio_kg >= 0),
    subtotal     NUMERIC(14,2) NOT NULL
);

CREATE INDEX idx_detalle_venta ON negocio.detalle_venta (venta_id);

-- ---------------------------------------------------------------------------
-- Gastos
-- ---------------------------------------------------------------------------
CREATE TABLE negocio.gasto (
    id           BIGSERIAL PRIMARY KEY,
    fecha        DATE         NOT NULL DEFAULT CURRENT_DATE,
    categoria    VARCHAR(60)  NOT NULL,   -- alimento, energia, mano_obra, mantenimiento...
    descripcion  VARCHAR(200),
    monto        NUMERIC(14,2) NOT NULL CHECK (monto >= 0),
    proveedor_id BIGINT       REFERENCES inventario.proveedor (id)
);

CREATE INDEX idx_gasto_fecha ON negocio.gasto (fecha);
