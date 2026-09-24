# Changelog

## 1.0.1 - 2026-09-24
- Añade `spring-boot-starter` para compilar y arrancar correctamente la aplicación Spring Boot.

## 1.0.0 - 2026-09-24
- Separa el parseo de STATS_PLAYER del consumer monolítico.
- Consume `file.ready.stats-player` y publica un mensaje Avro por jugador en `stats-player.parsed`.
- Elimina cualquier dependencia de PostgreSQL/JPA/Flyway.
