![version](https://img.shields.io/badge/version-2.2.0-blue)
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


## Publicación Kafka por chunks

KAN-72 elimina la espera `send(...).join()` por jugador. Cada chunk de hasta 500 jugadores se envía de forma asíncrona y se confirma con una única barrera `CompletableFuture.allOf(...)` antes de continuar. La key de cada jugador no cambia, por lo que se conserva el particionamiento existente. Un fallo de cualquier ACK hace fallar el procesamiento y mantiene la política retry/DLT.

## Contratos Avro compartidos

`FileEventKey`, `FileEventValue`, `StatsPlayerKey` y `StatsPlayerValue` se consumen desde `com.fernandez.basketball:basketball-event-contracts:1.0.2`. Este repositorio ya no mantiene copias locales de esos schemas ni genera las clases Avro durante su propia build.

Fuera de GitHub Actions, Maven necesita credenciales con `read:packages` para resolver el artefacto desde GitHub Packages.


## Estrategia de errores Kafka

KAN-108 aplica la política de KAN-18 al consumo de `file.ready.stats-player`.

- errores de validación, ruta o CSV: non-retryable;
- fallos transitorios de Kafka al publicar: retryable;
- intentos agotados: `file.ready.stats-player.DLT`;
- `KAFKA_RETRY_MAX_ATTEMPTS`: intentos totales, default `3`;
- `KAFKA_RETRY_BACKOFF_MS`: backoff fijo, default `1000`;
- `KAFKA_PLAYER_PARSER_DLT_TOPIC`: topic DLT configurable.

La DLT conserva el evento original y los headers de diagnóstico generados por Spring Kafka.


### Deserialización y DLT

Los deserializadores Avro están envueltos con `ErrorHandlingDeserializer`, de modo que un payload corrupto o incompatible entra en el flujo normal de recuperación. La DLT `file.ready.stats-player.DLT` admite objetos Avro y `byte[]` originales, conserva headers de diagnóstico, deja que Kafka seleccione una partición válida y propaga cualquier fallo de publicación.


### Wiring del publisher

El `KafkaTemplate` de DLT se construye internamente dentro del recoverer y no se registra como bean genérico. De esta forma Spring Boot mantiene el `KafkaTemplate<StatsPlayerKey, StatsPlayerValue>` usado por el publisher normal y la recuperación DLT conserva su soporte para Avro y `byte[]`.
