package com.example.csvstats.validation;

import com.example.csvstats.constants.CsvConstants;
import com.example.csvwatcher.watcher.FileEventKey;
import com.example.csvwatcher.watcher.FileEventValue;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class FileEventValidator {
    private final Path allowedRoot;

    public FileEventValidator(@Value("${app.csv.allowed-root:/data}") String allowedRoot) {
        this.allowedRoot = Path.of(allowedRoot).toAbsolutePath().normalize();
    }

    public Path validate(FileEventKey key, FileEventValue value) {
        if (key == null || key.getUniqueId() == null || key.getUniqueId().isBlank()) throw new IllegalArgumentException("Evento Kafka sin key valida");
        if (value == null || value.getFilePath() == null || value.getFilePath().isBlank()) throw new IllegalArgumentException("Evento Kafka sin filePath");
        Path path = Path.of(value.getFilePath());
        if (!path.isAbsolute() || !path.normalize().equals(path)) throw new IllegalArgumentException("filePath no valido: " + value.getFilePath());
        Path normalized = path.toAbsolutePath().normalize();
        if (!normalized.startsWith(allowedRoot)) throw new IllegalArgumentException("filePath fuera del directorio permitido: " + value.getFilePath());
        if (normalized.getFileName() == null || !CsvConstants.EXPECTED_FILENAME.equalsIgnoreCase(normalized.getFileName().toString())) throw new IllegalArgumentException("STATS_PLAYER requiere player_stats.csv");
        if (!Files.isRegularFile(normalized) || !Files.isReadable(normalized)) throw new IllegalArgumentException("Archivo inexistente o no legible: " + normalized);
        try (Reader reader = Files.newBufferedReader(normalized);
             CSVParser parser = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build().parse(reader)) {
            if (!CsvConstants.EXPECTED_HEADER.equals(parser.getHeaderNames())) throw new IllegalArgumentException("Cabecera no valida para STATS_PLAYER");
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException iae) throw iae;
            throw new IllegalArgumentException("No se puede validar el CSV: " + normalized, e);
        }
        return normalized;
    }
}
