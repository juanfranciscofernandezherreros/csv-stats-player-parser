# Changelog

## 1.0.0 - 2026-09-24
- Separa el parseo de STATS_PLAYER del consumer monolítico.
- Consume `file.ready.stats-player` y publica un mensaje Avro por jugador en `stats-player.parsed`.
- Elimina cualquier dependencia de PostgreSQL/JPA/Flyway.
