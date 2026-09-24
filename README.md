Current version: **2.0.0**

# csv-stats-player-parser

Microservicio que sustituye la parte de parseo de `csv-stats-player-consumer`.

```text
file.ready.stats-player -> FileReadyConsumer -> validación -> CSV parser -> Avro -> stats-player.parsed
```

No usa PostgreSQL, JPA ni Flyway. Valida `player_stats.csv`, su cabecera de 24 columnas y que el fichero esté dentro de `CSV_ALLOWED_ROOT`.

Variables principales: `KAFKA_BOOTSTRAP_SERVERS`, `KAFKA_SCHEMA_REGISTRY_URL`, `KAFKA_FILE_READY_TOPIC`, `KAFKA_PARSED_STATS_PLAYER_TOPIC`, `CSV_ALLOWED_ROOT`.

Test: `mvn -B test`.
