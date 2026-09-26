# Changelog

## 2.1.0 - 2026-09-26

- [minor] KAN-108 aplica la estrategia común de errores Kafka de KAN-18.
- [minor] Separa errores permanentes de validación/CSV de fallos transitorios de Kafka.
- [minor] Configura retries/backoff y DLT `file.ready.stats-player.DLT`.
- [minor] Añade tests de clasificación de error permanente y transitorio.


## 2.0.6 - 2026-09-25

- [patch] KAN-82 sustituye los schemas locales FileEvent/StatsPlayer por `basketball-event-contracts:1.0.2`.
- [patch] Elimina generación Avro local y configura CI con lectura autenticada desde GitHub Packages.
- [patch] Mantiene los namespaces, campos y semántica Kafka existentes sin cambios.

## 2.0.5 - 2026-09-25

- [patch] KAN-50 resuelve `CSV_ALLOWED_ROOT` y el fichero con `toRealPath()` antes de validar pertenencia.
- [patch] Bloquea symlinks dentro de la raíz que apunten fuera y conserva el uso de la ruta real validada.
- [patch] Añade tests específicos de fichero real permitido y escape mediante symlink.

## 2.0.4 - 2026-09-25

- [patch] Refuerza AGENTS.md con pre-flight obligatorio, autonomía y prohibición absoluta de escrituras directas en main.

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
