# Base de datos — piscicultura

Ejecuta los scripts **en este orden**:

1. `00_create_database.sql` — crea la base `piscicultura` (ejecútalo conectado a la base `postgres`).
2. `01_schema_inventario.sql` — esquema **inventario** (conectado ya a `piscicultura`).
3. `02_schema_produccion.sql` — esquema **produccion**.
4. `03_schema_negocio.sql` — esquema **negocio**.
5. `04_seed_data.sql` — datos de ejemplo (opcional).

## Modelo de datos

**inventario**: `proveedor`, `categoria_insumo`, `insumo`, `movimiento_inventario`
**produccion**: `especie`, `estanque`, `lote`, `biometria`, `mortalidad`, `registro_alimentacion`
**negocio**: `cliente`, `venta`, `detalle_venta`, `gasto`

Las áreas se relacionan entre sí: `registro_alimentacion` consume un `insumo`,
y `detalle_venta` puede referenciar un `lote` de producción.

## Credenciales

- Usuario: `postgres`
- Contraseña: `changeme-local-dev`
- Base: `piscicultura`
