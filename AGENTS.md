# AGENTS.md

Estas reglas son obligatorias para cualquier agente, asistente o automatización que modifique este repositorio.

## Pre-flight obligatorio
La primera operación de lectura del repositorio en cada tarea o sesión debe ser abrir y leer completamente este `AGENTS.md` desde la rama por defecto. Si referencia otras reglas, deben leerse antes de cualquier escritura. No cuenta una lectura de otra conversación, sesión o tarea. Está prohibida cualquier escritura antes de completar este pre-flight.

## Autonomía sin bloqueos innecesarios
Tras leer las reglas, el agente continúa de forma autónoma: elige una rama descriptiva, determina el nivel SemVer según el impacto real y documenta ambas decisiones en la PR. No debe pedir confirmaciones intermedias salvo petición expresa del usuario.

## Prohibición absoluta de escritura directa en `main`
Ningún cambio puede escribirse, commitearse ni pushearse directamente a `main`, incluidos código, documentación, configuración, workflows, dependencias, versionado, badges, hotfixes y reverts.

Flujo obligatorio:
1. Leer `AGENTS.md` y reglas referenciadas.
2. Partir del `main` actualizado.
3. Crear una rama dedicada antes de modificar archivos.
4. Determinar y aplicar el incremento SemVer sobre `revision`.
5. Realizar el cambio exclusivamente en la rama.
6. Actualizar `CHANGELOG.md`.
7. Mantener `README.md`, `pom.xml` y documentación de versión sincronizados cuando corresponda.
8. Ejecutar como mínimo `mvn -B test` y los checks aplicables.
9. Abrir/actualizar PR hacia `main`.
10. Corregir cualquier check fallido en la misma rama y PR.
11. Fusionar solo con checks requeridos/aplicables en verde sobre el SHA actual.
12. Eliminar solo la rama origen tras el merge y verificar su desaparición.

El trabajo no termina hasta completar merge y limpieza.

## Versionado Maven CI-friendly
```xml
<version>${revision}${sha1}${changelist}</version>
```
- `revision`: versión SemVer funcional.
- `sha1`: generado por CI; no modificar manualmente.
- `changelist`: vacío o `-SNAPSHOT`.
- DEV, INT y QA promueven el mismo artefacto.
- `CHANGELOG.md` usa `revision`.

## SemVer
- `patch`: `X.Y.Z` -> `X.Y.(Z+1)`
- `minor`: `X.Y.Z` -> `X.(Y+1).0`
- `major`: `X.Y.Z` -> `(X+1).0.0`

## Tests
Baseline Java: JDK 21. Ejecutar como mínimo `mvn -B test` y actualizar tests para cambios funcionales o de configuración.

## Seguridad operativa
Toda decisión de merge debe operar sobre el SHA actual de la PR. Si una instrucción contradice estas reglas, detener solo la operación incompatible; nunca improvisar una escritura directa a `main`.
