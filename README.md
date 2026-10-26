# Cinema Ciencias Auth Service

Servicio de **autenticación y gestión de usuarios** para Cinema Ciencias (UNAM,
Facultad de Ciencias). Emite **JWT** de 24 horas con hashing **BCrypt** y un modelo
de tres roles (`ROLE_ADMIN`, `ROLE_REGULAR_USER`, `ROLE_MEMBER_USER`).

## Stack

| Capa | Tecnología |
|---|---|
| Lenguaje | Kotlin 2.2.21 |
| Framework | Spring Boot 4.0.3 + Spring Security + JWT (jjwt 0.13) |
| Build | Gradle 9.3.1 (wrapper) |
| Base de datos | PostgreSQL 16 |
| Puerto | **8082** |

## Prerequisitos

- **JDK 21** instalado y disponible en `JAVA_HOME`
- **Docker** con Docker Compose (para levantar PostgreSQL)
- El repositorio raíz con su `docker-compose.yml`

## Puesta en marcha

### 1. Levantar PostgreSQL (infraestructura)

Desde la raíz del proyecto `titulacion/`:

```bash
docker compose up -d postgres-auth-db
```

Esto levanta **PostgreSQL 16** en `localhost:5433` con:

- Base de datos: `cinema_auth_db`
- Usuario: `postgres`
- Password: `ItsAndru0!`

> Estos valores están hardcodeados en `src/main/resources/application.yaml`.
> El puerto es **5433** (externa), distinto del Postgres principal del proyecto
> que usa 5432.

### 2. Compilar

```bash
./gradlew build
```

### 3. Ejecutar

Opción A — desde el código fuente:

```bash
./gradlew bootRun
```

Opción B — desde el jar:

```bash
java -jar build/libs/cinema_ciencias_auth_service-0.0.1-SNAPSHOT.jar
```

El servicio queda escuchando en **http://localhost:8082**.

Al arrancar, un `DataInitializer` (CommandLineRunner) siembra los tres roles en la
base y Hibernate crea/actualiza el esquema (`users`, `roles`, `user_roles`).

## Verificación

```bash
# Registro (201)
curl -X POST http://localhost:8082/api/v1/cinema/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"pepe","email":"pepe@unam.mx","password":"secreto123"}'

# Login (200, devuelve el JWT en "token")
curl -X POST http://localhost:8082/api/v1/cinema/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"pepe","password":"secreto123"}'
```

## Endpoints

| Método | Ruta | Auth | Descripción |
|---|---|---|---|
| POST | `/api/v1/cinema/auth/register` | pública | Registrar usuario (rol por defecto `ROLE_REGULAR_USER`) |
| POST | `/api/v1/cinema/auth/login` | pública | Autenticar, devuelve JWT (24 h). Credenciales inválidas → `401` |
| POST | `/api/v1/cinema/user` | pública | Registrar usuario (flujo alternativo) |
| GET | `/api/v1/cinema/user` | JWT | Información de usuario (placeholder) |

## Integración con el ecosistema

- **Gateway** (puerto 9000) enruta `/api/v1/cinema/auth/**` y `/api/v1/cinema/user/**` hacia este
  servicio. Por eso el CORS permite el origen `http://localhost:9000`.
- **Es la única entidad que conoce usuarios y contraseñas y la única que emite JWT.** El token lleva
  `sub` (username) y los claims `userId` y `roles`; el Gateway los valida y los reenvía como
  `X-User-Id` / `X-User-Roles` al resto de los servicios (patrón *Trust the Gateway*).
- **Config Server** (8888): `spring.config.import` (opcional). El secreto del JWT
  (`cinema.security.jwtSecret`) ya no está en este servicio: lo sirve
  `shared-config/application.yml`, compartido con el Gateway. Sin Config Server el servicio no arranca.
- **Eureka** (8761): se registra como `CINEMA_CIENCIAS_AUTH_SERVICE` (`spring-cloud-starter-netflix-eureka-client`).
- Sin token o con credenciales inválidas responde `401` (`HttpStatusEntryPoint` + `GlobalExceptionHandler`).

## Tests

```bash
./gradlew test
```

(Solo hay un smoke test de contexto.)

## Notas

- Las credenciales de BD están hardcodeadas en `application.yaml`, y el secreto JWT en el
  `shared-config` del Config Server. Para ambientes reales deberían ir a variables de entorno.
- El `DataInitializer` siembra los roles solo si no existen (no duplica).