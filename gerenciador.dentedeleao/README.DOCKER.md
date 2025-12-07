Run this project with Docker

Quick start (from repository root):

1) Build images and start services:

   docker-compose up --build

This composes two services:
 - db: Postgres 15 (database name: gerenciador_dente_de_leao, user: gerenciador_owner, password: changeme)
 - app: the Spring Boot application (exposed on host port 8080)

Notes and tips:
 - The application ships Flyway migrations. The schema name used by the app is `dente_de_leao_manager` and Flyway will create it on startup.
 - If you want a different DB password or port, edit `docker-compose.yml` and the corresponding SPRING_* env vars for the `app` service.
 - To pass a JWT secret, set the environment variable `JWT_SECRET` (the app reads this for api.security.token.secret via relaxed binding).

Troubleshooting:
 - If the app cannot connect to the DB on startup, make sure Docker Desktop is running and the Postgres container is healthy. You can inspect logs with `docker-compose logs db`.
 - To rebuild the app image after code changes: `docker-compose build --no-cache app` then `docker-compose up -d app`.

