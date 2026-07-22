# 🐟 Sistema de Inventario — Finca Piscícola

Sistema completo para administrar los datos del negocio de una finca de piscicultura, dividido en **tres áreas** (los tres datos del negocio):

| Área | Qué administra |
|------|----------------|
| **Inventario** | Insumos, proveedores, existencias y movimientos de stock |
| **Producción** | Estanques, especies, lotes de peces, biometría y mortalidad |
| **Negocio** | Clientes, ventas y gastos |

**Stack:** PostgreSQL · Java 17 + Spring Boot 3 · Angular 17

```
piscicultura-inventario/
├── database/     → scripts SQL (crear base y esquemas)
├── backend/      → API REST en Java (Spring Boot)
├── frontend/     → aplicación Angular
└── docker-compose.yml  → PostgreSQL + pgAdmin listos (opcional)
```

---

## 1. Base de datos

Las tres áreas se implementan como **tres esquemas** (`inventario`, `produccion`, `negocio`) dentro de una sola base **`piscicultura`**. Esto permite relaciones entre áreas y un único respaldo. Usuario `postgres`, contraseña **`changeme-local-dev`** por defecto (ver [Variables de entorno](#variables-de-entorno)).

El esquema lo gestiona **Flyway** desde el backend (`backend/src/main/resources/db/migration`), pero para crear la base **la primera vez** todavía necesitas uno de estos dos caminos:

### Opción A — con tu pgAdmin 4 (tu instalación actual)

1. Abre **pgAdmin 4** → `Servers` → tu servidor (contraseña `changeme-local-dev`).
2. Sobre **Databases** → clic derecho → *Query Tool* y ejecuta `database/00_create_database.sql`.
3. Arranca el backend (paso 2 más abajo): Flyway crea los esquemas, tablas y datos de ejemplo automáticamente al iniciar.

### Opción B — con Docker (no instala nada)

```bash
docker compose up -d
```
- pgAdmin: <http://localhost:5050> (admin@finca.co / changeme-local-dev)
- Los esquemas y datos de ejemplo se cargan solos (scripts en `/database`, los mismos que usa Flyway).

> Los archivos sueltos en `database/*.sql` siguen ahí por si prefieres ejecutarlos a mano o los usa Docker; `backend/src/main/resources/db/migration` tiene una copia versionada por Flyway (V1..V4) que es la que gestiona el esquema en cualquier base que arranque limpia.

---

## 2. Backend (Java / Spring Boot)

Requisitos: **JDK 17+** y **Maven** (o el wrapper `mvnw`).

```bash
cd backend
mvn spring-boot:run
```

- API: <http://localhost:8081/api>
- Documentación Swagger: <http://localhost:8081/api/swagger-ui.html>
- Tests: `mvn test` (unitarios de reglas de negocio + una prueba de integración de seguridad que necesita la base de datos activa).

La conexión a la base y las credenciales de acceso están en `backend/src/main/resources/application.yml`,
todas sobrescribibles por variable de entorno (ver abajo). Puerto por defecto **8081**
(ajusta `server.port` si en tu máquina el 8080 está libre y lo prefieres).

### Autenticación

La API está protegida con JWT. No hay gestión de usuarios: hay **un único usuario** configurado
por variables de entorno (`ADMIN_USERNAME` / `ADMIN_PASSWORD`, por defecto `admin` / `changeme-local-dev`).

```
POST /api/auth/login   { "username": "admin", "password": "changeme-local-dev" }
→ { "token": "...", "username": "admin" }
```

El resto de rutas (excepto `/api/auth/login` y Swagger) requieren `Authorization: Bearer <token>`.
El frontend ya maneja esto solo (pantalla de login + interceptor).

---

## 3. Frontend (Angular)

Requisitos: **Node.js 18+**.

```bash
cd frontend
npm install
npm start
```

Abre <http://localhost:4200> (te pedirá iniciar sesión, ver credenciales arriba). En desarrollo
apunta al backend en `localhost:8081` (ver `src/environments/environment.development.ts`).

- Tests: `npm test` (Karma/Jasmine; usa `npx ng test --watch=false --browsers=ChromeHeadless` para correrlos una sola vez sin abrir ventana).

---

## Variables de entorno

Copia `.env.example` a `.env` y ajusta si lo necesitas — todo tiene un valor por defecto igual
al usado en desarrollo, así que **nada se rompe si no creas el archivo**. Cubre credenciales de
Postgres/pgAdmin (para `docker-compose.yml`) y del backend (`DB_*`, `ADMIN_*`, `JWT_SECRET`).

---

## Orden recomendado para arrancar

1. Base de datos (Opción A o B).
2. Backend (`mvn spring-boot:run`) — Flyway deja el esquema listo al arrancar.
3. Frontend (`npm install` la primera vez, luego `npm start`).
4. Inicia sesión con `admin` / `changeme-local-dev` (o tus `ADMIN_USERNAME`/`ADMIN_PASSWORD`).

## Endpoints principales

Todos requieren `Authorization: Bearer <token>` salvo `/api/auth/login`.

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/auth/login` | Login, devuelve el JWT |
| GET/POST/PUT/DELETE | `/api/inventario/insumos` | Insumos |
| GET/POST | `/api/inventario/movimientos` | Entradas/salidas de stock (últimos 50) |
| GET/POST/PUT/DELETE | `/api/produccion/estanques` | Estanques |
| GET/POST/PUT/DELETE | `/api/produccion/lotes` | Lotes de peces |
| GET/POST/PUT/DELETE | `/api/negocio/clientes` | Clientes |
| GET/POST | `/api/negocio/ventas?page=0&size=20` | Ventas (paginadas) |
| GET | `/api/dashboard/resumen` | Indicadores del panel |
