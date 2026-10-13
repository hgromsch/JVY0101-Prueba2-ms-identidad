# ms-identidad

Identidad y perfiles de usuario de la plataforma Respirar y Soltar. Cubre **RF-01**.
Las credenciales y MFA se delegan a AWS Cognito; este servicio persiste el perfil de
negocio (nombre, email, rol) y los contactos de emergencia del usuario.

**Puerto:** 8081 · **Base de datos:** `identidad_db` · **Swagger:** http://localhost:8081/swagger-ui.html

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/usuarios` | Registra un usuario (201) |
| GET | `/api/usuarios?rol=` | Lista usuarios, filtro opcional por rol |
| GET | `/api/usuarios/{id}` | Obtiene un usuario (404 si no existe) |
| PUT | `/api/usuarios/{id}` | Actualiza el perfil |
| DELETE | `/api/usuarios/{id}` | Elimina el usuario (204) |
| POST | `/api/usuarios/{id}/contactos` | Agrega contacto de emergencia (201) |
| GET | `/api/usuarios/{id}/contactos` | Lista contactos del usuario |
| DELETE | `/api/usuarios/{id}/contactos/{contactoId}` | Elimina el contacto (204) |

Roles válidos: `PACIENTE`, `PSICOLOGO`, `ADMIN`.

## Cómo correr localmente

```bash
docker compose -f ../docker-compose.yml up -d   # Postgres + Redis (una sola vez)
mvn spring-boot:run
```

## Arquitectura

Capas `api` → `service` → `repository` → `domain`, con DTOs validados
(jakarta.validation) y errores centralizados en `GlobalExceptionHandler`.
Relación JPA destacada: `Usuario` **OneToMany** `ContactoEmergencia` (y **ManyToOne** inversa).

## Despliegue

Pensado para **Amazon ECS Fargate** detrás de API Gateway, con Aurora Serverless (PostgreSQL).
Imagen: `docker build -t ms-identidad .`
