# Changelog

## 2.0.3

- [patch] Estandariza la automatización del repositorio con el flujo autónomo de csv-results-parser.

## 2.0.2 - 2026-09-25

- [patch] Exige confirmar rama y nivel SemVer antes de cualquier cambio.
- [patch] Alinea Maven CI-friendly con revision, sha1 y changelist.

## 2.0.0 - 2026-09-24
- Consolida como contrato MAJOR la arquitectura parser Kafka sin PostgreSQL.
- Mantiene la publicación Avro en `stats-player.parsed` y documenta el gobierno común del repositorio.
- Alinea versión, README, CHANGELOG, AGENTS.md y CI con el resto de microservicios CSV.

## 1.0.1 - 2026-09-24
- Añade `spring-boot-starter` para compilar y arrancar correctamente la aplicación Spring Boot.

## 1.0.0 - 2026-09-24
- Separa el parseo de STATS_PLAYER del consumer monolítico.
- Consume `file.ready.stats-player` y publica un mensaje Avro por jugador en `stats-player.parsed`.
- Elimina cualquier dependencia de PostgreSQL/JPA/Flyway.
