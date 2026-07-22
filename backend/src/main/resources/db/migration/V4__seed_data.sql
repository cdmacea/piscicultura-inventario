-- ============================================================================
--  DATOS DE EJEMPLO (opcional). Ejecutar CONECTADO a "piscicultura".
-- ============================================================================

-- ---- INVENTARIO ----
INSERT INTO inventario.proveedor (nombre, nit, telefono, email) VALUES
 ('Alimentos Acuícolas del Valle', '900123456-1', '3001112233', 'ventas@acuicolasvalle.co'),
 ('AgroInsumos La Finca',          '901654321-2', '3004445566', 'contacto@agrolafinca.co');

INSERT INTO inventario.categoria_insumo (nombre, descripcion) VALUES
 ('Alimento',    'Concentrado y balanceado para peces'),
 ('Medicamento', 'Tratamientos y probióticos'),
 ('Equipo',      'Aireadores, redes, mallas'),
 ('Insumo',      'Cal, sal, fertilizantes');

INSERT INTO inventario.insumo
 (codigo, nombre, categoria_id, proveedor_id, unidad_medida, stock_actual, stock_minimo, precio_unitario) VALUES
 ('ALB-38', 'Balanceado 38% proteína', 1, 1, 'kg', 850.000, 200.000, 4200.00),
 ('ALB-45', 'Iniciación 45% proteína', 1, 1, 'kg', 120.000, 150.000, 5100.00),
 ('MED-OX', 'Oxitetraciclina 100g',    2, 2, 'unidad', 15.000, 5.000, 32000.00),
 ('EQ-AIR', 'Aireador 2HP',            3, 2, 'unidad', 3.000, 1.000, 1850000.00),
 ('INS-CAL','Cal agrícola 25kg',       4, 2, 'saco', 40.000, 10.000, 18000.00);

-- Movimiento inicial de ejemplo
INSERT INTO inventario.movimiento_inventario
 (insumo_id, tipo, cantidad, stock_resultante, motivo, usuario) VALUES
 (1, 'ENTRADA', 850.000, 850.000, 'Carga inicial de inventario', 'admin');

-- ---- PRODUCCIÓN ----
INSERT INTO produccion.especie (nombre_comun, nombre_cientifico, descripcion) VALUES
 ('Tilapia Roja', 'Oreochromis sp.',      'Especie de rápido crecimiento'),
 ('Cachama',      'Piaractus brachypomus','Resistente, buena en tierra'),
 ('Trucha',       'Oncorhynchus mykiss',  'Aguas frías');

INSERT INTO produccion.estanque (codigo, nombre, tipo, area_m2, volumen_m3, capacidad_max, estado) VALUES
 ('EST-01', 'Estanque Norte',  'GEOMEMBRANA', 300.00, 450.00, 6000, 'ACTIVO'),
 ('EST-02', 'Estanque Sur',    'TIERRA',      500.00, 600.00, 8000, 'ACTIVO'),
 ('EST-03', 'Tanque Concreto', 'CONCRETO',     40.00,  60.00, 1200, 'MANTENIMIENTO');

INSERT INTO produccion.lote
 (codigo, especie_id, estanque_id, fecha_siembra, cantidad_inicial, cantidad_actual, peso_promedio_g, estado) VALUES
 ('LOT-2025-01', 1, 1, DATE '2025-03-15', 5000, 4820, 180.50, 'ACTIVO'),
 ('LOT-2025-02', 2, 2, DATE '2025-04-02', 7000, 6950, 95.00,  'ACTIVO');

INSERT INTO produccion.biometria (lote_id, fecha, peso_promedio_g, longitud_promedio_cm, muestra_cantidad) VALUES
 (1, DATE '2025-05-01', 120.00, 14.5, 30),
 (1, DATE '2025-06-01', 180.50, 17.2, 30);

INSERT INTO produccion.mortalidad (lote_id, fecha, cantidad, causa) VALUES
 (1, DATE '2025-04-10', 120, 'Estrés por transporte'),
 (2, DATE '2025-04-20',  50, 'Causa no determinada');

-- ---- NEGOCIO ----
INSERT INTO negocio.cliente (nombre, tipo_documento, documento, telefono, email) VALUES
 ('Pescadería El Buen Sabor', 'NIT', '830456789-0', '3011234567', 'compras@buensabor.co'),
 ('Restaurante La Represa',   'NIT', '811222333-4', '3157654321', 'gerencia@larepresa.co'),
 ('Juan Pérez',               'CC',  '71234567',    '3009998877', 'juanp@correo.co');

INSERT INTO negocio.venta (codigo, cliente_id, fecha, total, estado, metodo_pago) VALUES
 ('VTA-0001', 1, DATE '2025-06-10', 900000.00, 'PAGADA',    'TRANSFERENCIA'),
 ('VTA-0002', 2, DATE '2025-06-18', 480000.00, 'PENDIENTE', 'CREDITO');

INSERT INTO negocio.detalle_venta (venta_id, lote_id, descripcion, cantidad_kg, precio_kg, subtotal) VALUES
 (1, 1, 'Tilapia entera fresca', 100.000, 9000.00, 900000.00),
 (2, 2, 'Cachama entera',         80.000, 6000.00, 480000.00);

INSERT INTO negocio.gasto (fecha, categoria, descripcion, monto, proveedor_id) VALUES
 (DATE '2025-06-01', 'alimento',  'Compra balanceado 38%', 3570000.00, 1),
 (DATE '2025-06-05', 'energia',   'Factura energía junio',  620000.00, NULL),
 (DATE '2025-06-08', 'mano_obra', 'Jornales cosecha',       800000.00, NULL);
