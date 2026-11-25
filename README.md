# LegalHub Backend

Backend REST de la Enciclopedia Interactiva de Derecho Informático.

## Stack

- Java 21
- Spring Boot 3.5.16
- Gradle 8.14.8 (wrapper)
- PostgreSQL 16
- Flyway
- Spring Security + JJWT
- springdoc-openapi

Todo el código, nombres de clases, variables y mensajes de commit van en inglés.

## Inicio rápido

```bash
# 1. Copiar variables de entorno
cp .env.example .env

# 2. Levantar PostgreSQL
docker compose up -d

# 3. Ejecutar la aplicación (Flyway aplica V1 automáticamente)
./gradlew bootRun
```

La API queda en `http://localhost:8080/api/v1`.

Swagger UI: `http://localhost:8080/swagger-ui.html`

Health check: `http://localhost:8080/actuator/health`

## Base de datos

La base de datos se crea vacía con el nombre, usuario y contraseña definidos en `.env` (`legalhub` por defecto). Flyway aplica `V1__init_schema.sql` al arrancar.

No se usa `ddl-auto=update`: Hibernate solo valida que las entidades coincidan con el esquema (`ddl-auto=validate`).

## Superadmin por defecto

| Variable | Valor por defecto |
| :--- | :--- |
| `SUPERADMIN_USERNAME` | `admin@unsis.com` |
| `SUPERADMIN_PASSWORD` | `admin123` |

La contraseña se guarda como hash BCrypt. El seeder solo inserta el usuario si no existe. Para forzar la actualización del hash, usar `SUPERADMIN_SYNC_PASSWORD=true` en `.env` y reiniciar.

## Estructura

```text
src/main/java/com/unsis/legalhub/
├── config/
├── security/
├── common/
├── auth/
├── superadmin/
├── unit/
├── subtopic/
├── content/
├── card/
└── question/

src/main/resources/
├── application.yml
├── application-dev.yml
└── db/migration/
    └── V1__init_schema.sql
```

## Comandos útiles

```bash
./gradlew bootRun          # arrancar
./gradlew build            # compilar y empaquetar
./gradlew test             # ejecutar tests
docker compose down        # detener PostgreSQL
```

Los contratos de API y de formularios están en los documentos `backend_legalhub.md` y `frontend_legalhub.md`.
