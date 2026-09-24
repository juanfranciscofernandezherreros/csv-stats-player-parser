package com.example.csvstats.parser;

import com.example.csvstats.dto.StatsPlayerDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
public class StatsPlayerCsvParser {
    private static final int EXPECTED_COLUMNS = 24;

    public void parseInChunks(Path path, int chunkSize, Consumer<List<StatsPlayerDTO>> consumer) throws IOException {
        if (chunkSize <= 0) throw new IllegalArgumentException("chunkSize debe ser mayor que 0");
        try (Reader reader = Files.newBufferedReader(path);
             CSVParser parser = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).setTrim(true).build().parse(reader)) {
            List<StatsPlayerDTO> chunk = new ArrayList<>(Math.min(chunkSize, 1000));
            for (CSVRecord record : parser) {
                if (record.size() != EXPECTED_COLUMNS) {
                    throw new IllegalArgumentException("Fila CSV " + record.getRecordNumber() + " con " + record.size() + " columnas; se esperaban " + EXPECTED_COLUMNS);
                }
                chunk.add(toDto(record));
                if (chunk.size() == chunkSize) {
                    consumer.accept(List.copyOf(chunk));
                    chunk.clear();
                }
            }
            if (!chunk.isEmpty()) consumer.accept(List.copyOf(chunk));
        }
    }

    private StatsPlayerDTO toDto(CSVRecord r) {
        StatsPlayerDTO d = new StatsPlayerDTO();
        d.setMatchId(r.get(0)); d.setName(r.get(1)); d.setTeam(r.get(2));
        d.setPts(integer(r,3)); d.setReb(integer(r,4)); d.setAst(integer(r,5)); d.setMin(r.get(6));
        d.setFgm(integer(r,7)); d.setFga(integer(r,8)); d.setTwopm(integer(r,9)); d.setTwopa(integer(r,10));
        d.setThreepm(integer(r,11)); d.setThreepa(integer(r,12)); d.setFtm(integer(r,13)); d.setFta(integer(r,14));
        d.setPlusMinus(integer(r,15)); d.setOr(integer(r,16)); d.setDr(integer(r,17)); d.setPf(integer(r,18));
        d.setSt(integer(r,19)); d.setTo(integer(r,20)); d.setBs(integer(r,21)); d.setBa(integer(r,22)); d.setTfs(integer(r,23));
        return d;
    }

    private Integer integer(CSVRecord r, int i) {
        String value = r.get(i);
        return "-".equals(value) || value.isBlank() ? null : Integer.valueOf(value);
    }
}
