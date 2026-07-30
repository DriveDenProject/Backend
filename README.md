# DriveDen Backend

API REST backend para DriveDen, una aplicación orientada al registro y seguimiento de información vehicular. El sistema permite gestionar usuarios, autenticación, vehículos, consumos de combustible, kilometraje, reparaciones, recordatorios de mantenimiento, tokens de dispositivos, suscripciones y procesamiento de entradas de voz relacionadas con registros vehiculares.

## Descripción

DriveDen Backend es una aplicación Java con Spring Boot que centraliza la lógica del dominio vehicular y expone endpoints HTTP para ser consumidos por clientes externos, como una aplicación web o móvil.

Su propósito principal es permitir que cada usuario mantenga el historial operativo de sus vehículos: datos del vehículo, cargas de combustible, odómetro, reparaciones, estadísticas, notificaciones de mantenimiento y uso de funcionalidades sujetas a planes de suscripción.

El proyecto integra servicios externos para autenticación con Google, consulta de especificaciones de carros y motos, envío de correos, notificaciones push y clasificación de texto mediante OpenAI.

## Features principales

- **Registro y autenticación de usuarios**
  - Registro de usuarios con contraseña encriptada.
  - Login con email y contraseña.
  - Login con Google mediante validación de ID token.
  - Emisión de access tokens y refresh tokens JWT.
  - Recuperación/cambio de contraseña mediante códigos de verificación por correo.

- **Gestión de vehículos**
  - Registro de vehículos asociados al usuario autenticado.
  - Consulta de marcas, modelos y generaciones de carros mediante un cliente externo de Car Specs.
  - Consulta de tipos de combustible y transmisión desde base de datos.
  - Consulta de vehículo principal del usuario.
  - Dashboard de vehículo e historial vehicular.

- **Registro de combustible y kilometraje**
  - Creación, consulta, actualización y eliminación de cargas de combustible.
  - Historial paginado de cargas de tanque.
  - Historial por rango de fechas.
  - Estadísticas de combustible del mes actual.
  - Estadísticas mensuales y del mes actual de kilometraje.
  - Registro de odómetro asociado a operaciones vehiculares.

- **Reparaciones y partes**
  - Registro de reparaciones por vehículo.
  - Historial paginado de reparaciones.
  - Estadísticas de reparaciones.
  - Consulta de últimas reparaciones por categoría.
  - Consulta de categorías de partes.

- **Notificaciones de mantenimiento**
  - CRUD de notificaciones vehiculares.
  - Consulta por usuario o vehículo.
  - Cambio de estado y marcado como completada.
  - Despacho manual de la última notificación.
  - Scheduler horario para procesar notificaciones pendientes.
  - Envío de push notifications mediante Firebase Cloud Messaging cuando está configurado.

- **Dispositivos**
  - Registro de tokens de dispositivos por usuario autenticado.
  - Soporte de plataformas definido en el dominio de device tokens.

- **Suscripciones**
  - Consulta de suscripción actual.
  - Creación automática de suscripción gratuita cuando no existe una suscripción activa.
  - Consulta de planes activos.
  - Límites por plan para creación de vehículos y uso mensual de audio/escaneos.
  - Grant administrativo de suscripciones.
  - La activación con Google Play existe como endpoint, pero el servicio responde explícitamente que todavía no está implementada.

- **Procesamiento de voz**
  - Endpoint para procesar texto proveniente de entrada de voz.
  - Prefiltrado, rate limiting, cache de duplicados y clasificación con OpenAI.
  - Clasificación orientada a extraer registros vehiculares en formato JSON.
  - Consumo de uso mensual de audio cuando la clasificación es válida.

- **Catálogos externos de motos**
  - Consulta de marcas, modelos, años de producción, article ID e imagen de motos mediante cliente externo configurado con RapidAPI.

## Tecnologías utilizadas

### Lenguaje

- Java 21

### Backend

- Spring Boot 3.5.10
- Spring Web
- Spring WebFlux `WebClient`
- Spring Validation
- Spring Security
- Spring Scheduling
- Spring Mail

### Persistencia y base de datos

- PostgreSQL
- Spring Data JPA
- Spring JDBC
- Flyway
- HikariCP
- H2 para pruebas

### Seguridad

- JWT con `com.auth0:java-jwt`
- BCrypt para hashing de contraseñas
- Autenticación stateless con filtro personalizado
- Google OAuth/OpenID Connect mediante Google API Client

### Integraciones externas

- RapidAPI Car Specs
- RapidAPI Motorcycle Specs Database
- Brevo SMTP para correo transaccional
- Firebase Admin SDK para push notifications
- OpenAI Chat Completions API mediante `WebClient`

### Testing

- Spring Boot Starter Test
- JUnit 5
- Mockito
- H2 en memoria para configuración de pruebas

### Infraestructura y deployment

- Docker multi-stage build
- Docker Compose con servicio backend y PostgreSQL
- Railway/Railpack mediante `app/railpack.toml`
- Maven Wrapper

### Herramientas y librerías

- Maven
- Lombok

## Arquitectura

El proyecto usa una arquitectura por capas con un enfoque hexagonal parcial. La evidencia está en la separación entre dominio, aplicación, puertos de salida e infraestructura:

- `domain`: modelos, DTOs, enumeraciones y excepciones de negocio.
- `application`: servicios de aplicación y casos de uso que coordinan reglas de negocio.
- `application/ports/out`: contratos para dependencias externas o persistencia.
- `infrastructure/controllers/in/web`: adaptadores de entrada HTTP mediante controladores REST.
- `infrastructure/out`: adaptadores de salida para persistencia, OAuth, notificaciones y proyecciones.
- `config`: configuración de seguridad, clientes HTTP, Firebase y OpenAI.
- `common`: manejo transversal de errores.

Flujo general:

1. Un cliente consume un endpoint REST.
2. Spring Security valida el JWT, excepto rutas públicas como `/auth/**` y `/users/post`.
3. El controlador obtiene el usuario autenticado cuando aplica y delega en un servicio o caso de uso.
4. La capa de aplicación ejecuta reglas de negocio y usa repositorios o puertos.
5. Los adaptadores de infraestructura implementan persistencia con JPA, llamadas HTTP externas, OAuth o Firebase.
6. Las respuestas se envuelven en `CustomResponse`; los errores se centralizan en `GlobalExceptionHandler`.

No es una arquitectura hexagonal estricta en todos los módulos, porque algunos servicios usan directamente repositorios concretos de infraestructura. Sin embargo, varios componentes sí siguen el patrón de puertos y adaptadores, especialmente suscripciones, reparaciones, odómetro, combustible, historial, Google OAuth y push notifications.

## Estructura del proyecto

```text
DriveDenBack/
├── README.md
├── Dockerfile
├── docker-compose.yml
└── app/
    ├── pom.xml
    ├── mvnw
    ├── mvnw.cmd
    ├── railpack.toml
    └── src/
        ├── main/
        │   ├── java/com/driveden/app/
        │   │   ├── AppApplication.java
        │   │   ├── application/
        │   │   │   ├── ports/out/
        │   │   │   ├── services/
        │   │   │   └── usecase/
        │   │   ├── client/
        │   │   ├── common/
        │   │   ├── config/
        │   │   ├── domain/
        │   │   ├── infrastructure/
        │   │   │   ├── ai/
        │   │   │   ├── controllers/in/web/
        │   │   │   ├── out/
        │   │   │   └── scheduler/
        │   │   └── utils/
        │   └── resources/
        │       ├── application.properties
        │       └── db/migration/
        └── test/
            ├── java/com/driveden/app/
            └── resources/application.properties
```

### Carpetas principales

- `application/services`: servicios de aplicación para autenticación, usuarios, vehículos, combustible, reparaciones, suscripciones, notificaciones, dispositivos y estadísticas.
- `application/usecase`: casos de uso específicos, actualmente enfocados en procesamiento de entrada de voz.
- `application/ports/out`: interfaces para repositorios, Google OAuth y push notifications.
- `domain`: objetos de dominio, DTOs y excepciones agrupados por contexto funcional.
- `infrastructure/controllers/in/web`: controladores REST.
- `infrastructure/out/persistence`: entidades JPA, repositorios Spring Data, implementaciones de repositorio, mappers y proyecciones.
- `infrastructure/ai`: integración con OpenAI, construcción de prompts, parser, prefiltrado, rate limiter y clasificador.
- `client`: clientes HTTP para APIs externas de carros y motos.
- `config`: configuración de seguridad, clientes HTTP, Firebase y OpenAI.
- `db/migration`: migraciones Flyway SQL.

## Requisitos

Para ejecutar localmente:

- Java 21
- Maven o Maven Wrapper incluido
- PostgreSQL
- Variables de entorno requeridas por `application.properties`

Para ejecutar con Docker Compose:

- Docker
- Docker Compose

## Instalación y ejecución

### Opción 1: ejecución local con Maven Wrapper

1. Entrar al módulo de la aplicación:

```bash
cd app
```

2. Configurar variables de entorno. Ejemplo:

```bash
DB_URL=jdbc:postgresql://localhost:5432/driveden
DB_USER=driveden
DB_PASSWORD=driveden
SECRET_TOKEN=replace-with-a-secure-secret
BREVO_KEY=replace-if-used
BREVO_HOST=smtp-relay.brevo.com
BREVO_PORT=587
BREVO_USER=replace-if-used
BREVO_PASSWORD=replace-if-used
EMAIL_SENDER=no-reply@example.com
RAPIDAPI_KEY=replace-if-used
RAPIDAPI_HOST=replace-if-used
RAPIDAPI_MOTORCYCLE_KEY=replace-if-used
RAPIDAPI_MOTORCYCLE_HOST=replace-if-used
GOOGLE_OAUTH_CLIENT_ID=replace-if-used
FIREBASE_ENABLED=false
FIREBASE_CONFIG_PATH=
FIREBASE_SERVICE_ACCOUNT_JSON=
OPENAI_API_KEY=replace-if-used
```

3. Ejecutar la aplicación:

```bash
./mvnw spring-boot:run
```

En Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

### Opción 2: ejecución con Docker Compose

Desde la raíz del repositorio:

```bash
docker compose up --build
```

El compose levanta:

- `backend`: API Spring Boot.
- `postgres`: PostgreSQL 16 Alpine con volumen persistente.

Por defecto, el backend se publica en `http://localhost:8080`.

### Tests

Desde `app/`:

```bash
./mvnw test
```

En Windows:

```powershell
.\mvnw.cmd test
```

## Configuración

La configuración principal está en `app/src/main/resources/application.properties`.

| Variable | Uso |
| --- | --- |
| `DB_URL` | URL JDBC de PostgreSQL |
| `DB_USER` | Usuario de base de datos |
| `DB_PASSWORD` | Contraseña de base de datos |
| `SECRET_TOKEN` | Secreto para firmar/verificar JWT |
| `BREVO_KEY` | API key configurada para Brevo |
| `BREVO_HOST` | Host SMTP |
| `BREVO_PORT` | Puerto SMTP |
| `BREVO_USER` | Usuario SMTP |
| `BREVO_PASSWORD` | Contraseña SMTP |
| `EMAIL_SENDER` | Remitente de correos |
| `RAPIDAPI_KEY` | API key para Car Specs |
| `RAPIDAPI_HOST` | Host RapidAPI para Car Specs |
| `RAPIDAPI_MOTORCYCLE_KEY` | API key para Motorcycle Specs Database |
| `RAPIDAPI_MOTORCYCLE_HOST` | Host RapidAPI para Motorcycle Specs Database |
| `GOOGLE_OAUTH_CLIENT_ID` | Client ID para Google Sign-In |
| `FIREBASE_ENABLED` | Habilita/deshabilita inicialización de Firebase |
| `FIREBASE_CONFIG_PATH` | Ruta al service account de Firebase |
| `FIREBASE_SERVICE_ACCOUNT_JSON` | Credenciales Firebase en JSON como variable de entorno |
| `OPENAI_API_KEY` | API key para OpenAI |

Notas:

- Flyway está habilitado en runtime y deshabilitado en pruebas.
- `server.error.include-stacktrace=never` evita exponer stack traces en respuestas de error.
- HikariCP está configurado con un pool pequeño y opciones compatibles con PgBouncer/poolers.
- Firebase se inicializa solo si `firebase.enabled=true` o si la propiedad no se define. En Docker Compose se establece `FIREBASE_ENABLED=false` por defecto.

## API / Uso

La API responde con objetos envueltos en `CustomResponse`. La mayoría de endpoints requiere header:

```http
Authorization: Bearer <access_token>
```

### Autenticación y usuarios

| Método | Endpoint | Descripción |
| --- | --- | --- |
| `POST` | `/users/post` | Registra un usuario |
| `POST` | `/auth/login` | Login con email y contraseña |
| `POST` | `/auth/google` | Login con Google ID token |
| `POST` | `/auth/refresh` | Renueva access/refresh tokens |
| `GET` | `/auth/me` | Devuelve el principal autenticado |
| `POST` | `/auth/send-code` | Envía código de verificación por email |
| `POST` | `/auth/verify-code` | Valida código de verificación |
| `POST` | `/auth/change-password` | Cambia contraseña usando código |
| `GET` | `/users/getByEmail` | Busca usuario por email |
| `GET` | `/users/primary-vehicle` | Obtiene detalles del vehículo principal del usuario autenticado |

### Vehículos, combustible y kilometraje

| Método | Endpoint | Descripción |
| --- | --- | --- |
| `GET` | `/cars/all-makes` | Consulta marcas de carros |
| `GET` | `/cars/models` | Consulta modelos por marca |
| `GET` | `/cars/models/generations` | Consulta generaciones por modelo |
| `GET` | `/cars/fuel-type` | Lista tipos de combustible |
| `GET` | `/cars/transmission-type` | Lista tipos de transmisión |
| `POST` | `/cars/register` | Registra un vehículo |
| `POST` | `/cars/fuel-logs` | Registra una carga de combustible |
| `GET` | `/cars/fuel-logs` | Lista cargas de combustible por vehículo |
| `GET` | `/cars/fuel-logs/history` | Consulta historial de combustible paginado o por fechas |
| `GET` | `/cars/fuel-logs/stats/current-month` | Estadísticas de combustible del mes actual |
| `GET` | `/cars/fuel-logs/latest` | Últimas cargas de combustible |
| `PUT` | `/cars/fuel-logs/{fuelLogId}` | Actualiza una carga de combustible |
| `DELETE` | `/cars/fuel-logs/{fuelLogId}` | Elimina una carga de combustible |
| `GET` | `/cars/history` | Historial vehicular paginado |
| `GET` | `/cars/dashboard` | Dashboard de vehículo |
| `GET` | `/cars/mileage-stats/monthly` | Estadísticas mensuales de kilometraje |
| `GET` | `/cars/mileage-stats/current-month` | Estadísticas de kilometraje del mes actual |

### Reparaciones

| Método | Endpoint | Descripción |
| --- | --- | --- |
| `POST` | `/repairs` | Registra reparación |
| `GET` | `/repairs/history` | Historial paginado de reparaciones |
| `GET` | `/repairs/stats` | Estadísticas de reparaciones |
| `GET` | `/repairs/latest-by-category` | Últimas reparaciones por categoría |
| `GET` | `/part-categories` | Lista categorías de partes |

### Notificaciones y dispositivos

| Método | Endpoint | Descripción |
| --- | --- | --- |
| `POST` | `/vehicle-notifications` | Crea notificación vehicular |
| `GET` | `/vehicle-notifications` | Lista notificaciones del usuario |
| `GET` | `/vehicle-notifications/vehicle` | Lista notificaciones por vehículo |
| `GET` | `/vehicle-notifications/{notificationId}` | Obtiene una notificación |
| `PUT` | `/vehicle-notifications/{notificationId}` | Actualiza una notificación |
| `DELETE` | `/vehicle-notifications/{notificationId}` | Elimina una notificación |
| `PATCH` | `/vehicle-notifications/{notificationId}/complete` | Marca notificación como completada |
| `PATCH` | `/vehicle-notifications/{notificationId}/status` | Actualiza estado |
| `POST` | `/vehicle-notifications/latest/dispatch` | Procesa despacho de última notificación |
| `POST` | `/api/device-tokens` | Registra token de dispositivo |

### Suscripciones y pagos

| Método | Endpoint | Descripción |
| --- | --- | --- |
| `GET` | `/subscriptions/me` | Consulta suscripción actual |
| `GET` | `/subscriptions/plans` | Lista planes activos |
| `POST` | `/subscriptions/activate` | Endpoint existente, activación Google Play no implementada |
| `POST` | `/admin/subscriptions/grant` | Otorga un plan a un usuario |
| `GET` | `/payment-methods/available` | Lista métodos de pago disponibles |

### Voz y motos

| Método | Endpoint | Descripción |
| --- | --- | --- |
| `POST` | `/voice-input/process` | Procesa texto de entrada de voz vehicular |
| `GET` | `/motorcycles/makes` | Consulta marcas de motos |
| `GET` | `/motorcycles/models` | Consulta modelos por marca |
| `GET` | `/motorcycles/models/{modelId}/years` | Consulta años de producción |
| `GET` | `/motorcycles/articles/id` | Obtiene article ID por año, marca y modelo |
| `GET` | `/motorcycles/articles/{articleId}/image-link` | Obtiene enlace de imagen de moto |

## Seguridad

- Spring Security está configurado en modo stateless.
- CSRF está deshabilitado para la API.
- `/auth/**` y `/users/post` son rutas públicas.
- El resto de rutas requiere autenticación JWT.
- `SecurityFilter` extrae el token `Bearer`, valida firma e issuer, y construye un `AuthenticatedUser`.
- Los refresh tokens incluyen claim `type=refresh` y no son aceptados como access tokens por el filtro.
- Las contraseñas se almacenan usando `BCryptPasswordEncoder`.
- Los DTOs usan validaciones de Jakarta Validation.
- Los errores de validación y excepciones se manejan con `GlobalExceptionHandler`.
- La configuración evita exponer stack traces en respuestas HTTP.

## Base de datos

El proyecto usa PostgreSQL como motor principal y Flyway para versionar el esquema.

Las migraciones se encuentran en:

```text
app/src/main/resources/db/migration/
```

Entidades y tablas principales:

- `users`
- `user_auth_providers`
- `email_verification_codes`
- `vehicles`
- `user_vehicles`
- `vehicle_details`
- `fuel_types`
- `transmission_types`
- `fuel_logs`
- `odometer_logs`
- `part_categories`
- `parts`
- `repairs`
- `repair_parts`
- `maintenance_categories`
- `vehicle_notifications`
- `user_device_tokens`
- `payment_methods`
- `subscription_plans`
- `user_subscriptions`
- `subscription_features`
- `plan_features`
- `payment_transactions`
- `subscription_usage`

El modelo de persistencia está implementado con entidades JPA, repositorios Spring Data JPA, mappers entre entidades y dominio, y algunas proyecciones para consultas de historial y estadísticas.

## Deployment

### Docker

El `Dockerfile` usa build multi-stage:

1. Compila con `maven:3.9.9-eclipse-temurin-21`.
2. Empaqueta el JAR con tests omitidos.
3. Ejecuta en `eclipse-temurin:21-jre-alpine`.
4. Expone el puerto `8080`.

### Docker Compose

`docker-compose.yml` define:

- Backend Spring Boot.
- PostgreSQL 16 Alpine.
- Volumen persistente para datos.
- Healthcheck de PostgreSQL.
- Red interna `driveden-network`.

### Railway / Railpack

`app/railpack.toml` define:

```toml
[options]
buildCommand = "./mvnw -B package -DskipTests"

[deploy]
startCommand = "java -jar target/app-0.0.1-SNAPSHOT.jar"
```

## Estado del proyecto

El repositorio contiene un backend funcional en desarrollo con módulos implementados para autenticación, usuarios, vehículos, combustible, kilometraje, reparaciones, notificaciones, dispositivos, suscripciones y procesamiento de voz.

Existen integraciones preparadas con servicios externos, pero su operación depende de variables de entorno y credenciales válidas. La activación de suscripciones mediante Google Play está declarada como no implementada en el propio servicio.

