-- ============================================================================
--  PISCICULTURA - Sistema de Inventario
--  00_create_database.sql
--  Ejecutar CONECTADO a la base "postgres" (o cualquiera distinta a la nueva).
--
--  En pgAdmin 4:
--    Servers > (tu servidor) > click derecho en "Databases" > Query Tool
--    y pega este archivo. Usuario: postgres  |  Contraseña: changeme-local-dev
-- ============================================================================

-- Crea la base de datos principal del negocio.
-- (Si ya existe, comenta esta línea o elimínala antes desde pgAdmin.)
CREATE DATABASE piscicultura
    WITH
    ENCODING = 'UTF8'
    LC_COLLATE = 'es_CO.UTF-8'
    LC_CTYPE = 'es_CO.UTF-8'
    TEMPLATE = template0
    CONNECTION LIMIT = -1;

COMMENT ON DATABASE piscicultura
    IS 'Sistema de inventario y administración para finca de piscicultura';

-- ----------------------------------------------------------------------------
--  NOTA SOBRE "3 BASES DE DATOS"
--  El sistema separa el negocio en TRES áreas (los "3 datos de negocio"):
--     1. inventario  -> insumos, proveedores, movimientos de stock
--     2. produccion  -> estanques, especies, lotes, biometría, mortalidad
--     3. negocio     -> clientes, ventas, gastos
--
--  Se implementan como 3 ESQUEMAS dentro de una sola base "piscicultura".
--  Ventaja: permiten relaciones (llaves foráneas) entre áreas y un único
--  backup, manteniendo la separación lógica. Si necesitas 3 bases físicas
--  independientes, crea 3 CREATE DATABASE y ajusta la conexión del backend.
-- ----------------------------------------------------------------------------

-- Después de crear la base, CONÉCTATE a "piscicultura" y ejecuta en orden:
--   01_schema_inventario.sql
--   02_schema_produccion.sql
--   03_schema_negocio.sql
--   04_seed_data.sql
