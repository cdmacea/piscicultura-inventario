-- ============================================================================
--  ÁREA 2: PRODUCCIÓN
--  Ejecutar CONECTADO a la base "piscicultura" (después de 01_...).
-- ============================================================================

CREATE SCHEMA IF NOT EXISTS produccion;
COMMENT ON SCHEMA produccion IS 'Estanques, especies, lotes, biometría y mortalidad';

-- ---------------------------------------------------------------------------
-- Especies (tilapia, cachama, trucha, ...)
-- ---------------------------------------------------------------------------
CREATE TABLE produccion.especie (
    id               BIGSERIAL PRIMARY KEY,
    nombre_comun     VARCHAR(100) NOT NULL UNIQUE,
    nombre_cientifico VARCHAR(120),
    descripcion      VARCHAR(300)
);

-- ---------------------------------------------------------------------------
-- Estanques / tanques / jaulas
-- ---------------------------------------------------------------------------
CREATE TABLE produccion.estanque (
    id            BIGSERIAL PRIMARY KEY,
    codigo        VARCHAR(40)  NOT NULL UNIQUE,
    nombre        VARCHAR(120) NOT NULL,
    tipo          VARCHAR(20)  NOT NULL DEFAULT 'TIERRA'
                  CHECK (tipo IN ('TIERRA','GEOMEMBRANA','CONCRETO','JAULA')),
    area_m2       NUMERIC(12,2),
    volumen_m3    NUMERIC(12,2),
    capacidad_max INTEGER,
    estado        VARCHAR(20)  NOT NULL DEFAULT 'ACTIVO'
                  CHECK (estado IN ('ACTIVO','MANTENIMIENTO','INACTIVO')),
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- ---------------------------------------------------------------------------
-- Lotes (siembra de peces en un estanque)
-- ---------------------------------------------------------------------------
CREATE TABLE produccion.lote (
    id                BIGSERIAL PRIMARY KEY,
    codigo            VARCHAR(40)  NOT NULL UNIQUE,
    especie_id        BIGINT       NOT NULL REFERENCES produccion.especie (id),
    estanque_id       BIGINT       NOT NULL REFERENCES produccion.estanque (id),
    fecha_siembra     DATE         NOT NULL,
    cantidad_inicial  INTEGER      NOT NULL CHECK (cantidad_inicial > 0),
    cantidad_actual   INTEGER      NOT NULL,
    peso_promedio_g   NUMERIC(10,2) NOT NULL DEFAULT 0,
    estado            VARCHAR(20)  NOT NULL DEFAULT 'ACTIVO'
                      CHECK (estado IN ('ACTIVO','COSECHADO','PERDIDO')),
    observaciones     VARCHAR(300),
    created_at        TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_lote_estanque ON produccion.lote (estanque_id);
CREATE INDEX idx_lote_especie  ON produccion.lote (especie_id);
CREATE INDEX idx_lote_estado   ON produccion.lote (estado);

-- ---------------------------------------------------------------------------
-- Biometría (muestreos de peso/talla)
-- ---------------------------------------------------------------------------
CREATE TABLE produccion.biometria (
    id                 BIGSERIAL PRIMARY KEY,
    lote_id            BIGINT       NOT NULL REFERENCES produccion.lote (id) ON DELETE CASCADE,
    fecha              DATE         NOT NULL DEFAULT CURRENT_DATE,
    peso_promedio_g    NUMERIC(10,2) NOT NULL,
    longitud_promedio_cm NUMERIC(8,2),
    muestra_cantidad   INTEGER,
    observaciones      VARCHAR(300)
);

CREATE INDEX idx_biometria_lote ON produccion.biometria (lote_id);

-- ---------------------------------------------------------------------------
-- Mortalidad
-- ---------------------------------------------------------------------------
CREATE TABLE produccion.mortalidad (
    id        BIGSERIAL PRIMARY KEY,
    lote_id   BIGINT     NOT NULL REFERENCES produccion.lote (id) ON DELETE CASCADE,
    fecha     DATE       NOT NULL DEFAULT CURRENT_DATE,
    cantidad  INTEGER    NOT NULL CHECK (cantidad > 0),
    causa     VARCHAR(200)
);

CREATE INDEX idx_mortalidad_lote ON produccion.mortalidad (lote_id);

-- ---------------------------------------------------------------------------
-- Registro de alimentación (consume insumos del área inventario)
-- Relación entre áreas: produccion -> inventario
-- ---------------------------------------------------------------------------
CREATE TABLE produccion.registro_alimentacion (
    id            BIGSERIAL PRIMARY KEY,
    lote_id       BIGINT       NOT NULL REFERENCES produccion.lote (id) ON DELETE CASCADE,
    insumo_id     BIGINT       NOT NULL REFERENCES inventario.insumo (id),
    fecha         DATE         NOT NULL DEFAULT CURRENT_DATE,
    cantidad_kg   NUMERIC(12,3) NOT NULL CHECK (cantidad_kg > 0),
    observaciones VARCHAR(300)
);

CREATE INDEX idx_alimentacion_lote   ON produccion.registro_alimentacion (lote_id);
CREATE INDEX idx_alimentacion_insumo ON produccion.registro_alimentacion (insumo_id);
