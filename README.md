![version](https://img.shields.io/badge/version-2.0.6-blue)
# csv-stats-player-parser

Microservicio que sustituye la parte de parseo de `csv-stats-player-consumer`.

```text
file.ready.stats-player -> FileReadyConsumer -> validación -> CSV parser -> Avro -> stats-player.parsed
```

No usa PostgreSQL, JPA ni Flyway. Valida `player_stats.csv`, su cabecera de 24 columnas y que el fichero esté dentro de `CSV_ALLOWED_ROOT`.

Variables principales: `KAFKA_BOOTSTRAP_SERVERS`, `KAFKA_SCHEMA_REGISTRY_URL`, `KAFKA_FILE_READY_TOPIC`, `KAFKA_PARSED_STATS_PLAYER_TOPIC`, `CSV_ALLOWED_ROOT`.

Test: `mvn -B test`.


## Seguridad de rutas

La validación resuelve tanto `CSV_ALLOWED_ROOT` como `player_stats.csv` con `toRealPath()`. Un symlink situado dentro de la raíz permitida que resuelva fuera es rechazado antes del parsing.


## Contratos Avro compartidos

`FileEventKey`, `FileEventValue`, `StatsPlayerKey` y `StatsPlayerValue` se consumen desde `com.fernandez.basketball:basketball-event-contracts:1.0.2`. Este repositorio ya no mantiene copias locales de esos schemas ni genera las clases Avro durante su propia build.

Fuera de GitHub Actions, Maven necesita credenciales con `read:packages` para resolver el artefacto desde GitHub Packages.
