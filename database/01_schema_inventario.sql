-- ============================================================================
--  ÁREA 1: INVENTARIO
--  Ejecutar CONECTADO a la base "piscicultura".
-- ============================================================================

CREATE SCHEMA IF NOT EXISTS inventario;
COMMENT ON SCHEMA inventario IS 'Insumos, proveedores y movimientos de stock';

-- ---------------------------------------------------------------------------
-- Proveedores
-- ---------------------------------------------------------------------------
CREATE TABLE inventario.proveedor (
    id          BIGSERIAL PRIMARY KEY,
    nombre      VARCHAR(150) NOT NULL,
    nit         VARCHAR(30)  UNIQUE,
    telefono    VARCHAR(30),
    email       VARCHAR(120),
    direccion   VARCHAR(200),
    activo      BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- ---------------------------------------------------------------------------
-- Categorías de insumo (alimento, medicamento, equipo, etc.)
-- ---------------------------------------------------------------------------
CREATE TABLE inventario.categoria_insumo (
    id          BIGSERIAL PRIMARY KEY,
    nombre      VARCHAR(80) NOT NULL UNIQUE,
    descripcion VARCHAR(200)
);

-- ---------------------------------------------------------------------------
-- Insumos (lo que se controla en el inventario)
-- ---------------------------------------------------------------------------
CREATE TABLE inventario.insumo (
    id              BIGSERIAL PRIMARY KEY,
    codigo          VARCHAR(40)  NOT NULL UNIQUE,
    nombre          VARCHAR(150) NOT NULL,
    categoria_id    BIGINT       NOT NULL REFERENCES inventario.categoria_insumo (id),
    proveedor_id    BIGINT       REFERENCES inventario.proveedor (id),
    unidad_medida   VARCHAR(20)  NOT NULL DEFAULT 'kg',   -- kg, unidad, litro, saco...
    stock_actual    NUMERIC(14,3) NOT NULL DEFAULT 0,
    stock_minimo    NUMERIC(14,3) NOT NULL DEFAULT 0,
    precio_unitario NUMERIC(14,2) NOT NULL DEFAULT 0,
    activo          BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_stock_no_negativo CHECK (stock_actual >= 0)
);

CREATE INDEX idx_insumo_categoria ON inventario.insumo (categoria_id);
CREATE INDEX idx_insumo_activo    ON inventario.insumo (activo);

-- ---------------------------------------------------------------------------
-- Movimientos de inventario (entradas, salidas y ajustes)
-- El backend actualiza inventario.insumo.stock_actual al registrar cada uno.
-- ---------------------------------------------------------------------------
CREATE TABLE inventario.movimiento_inventario (
    id          BIGSERIAL PRIMARY KEY,
    insumo_id   BIGINT       NOT NULL REFERENCES inventario.insumo (id),
    tipo        VARCHAR(10)  NOT NULL CHECK (tipo IN ('ENTRADA','SALIDA','AJUSTE')),
    cantidad    NUMERIC(14,3) NOT NULL CHECK (cantidad > 0),
    stock_resultante NUMERIC(14,3) NOT NULL,
    motivo      VARCHAR(200),
    usuario     VARCHAR(80),
    fecha       TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_mov_insumo ON inventario.movimiento_inventario (insumo_id);
CREATE INDEX idx_mov_fecha  ON inventario.movimiento_inventario (fecha);
