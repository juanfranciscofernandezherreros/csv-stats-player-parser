package com.example.csvstats.validation;

import com.example.csvstats.constants.CsvConstants;
import com.example.csvwatcher.watcher.FileEventKey;
import com.example.csvwatcher.watcher.FileEventValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FileEventValidatorTest {

    @TempDir
    Path tempDir;

    @Test
    void acceptsRealPlayerStatsFileInsideAllowedRoot() throws Exception {
        Path allowed = Files.createDirectory(tempDir.resolve("allowed"));
        Path csv = allowed.resolve("player_stats.csv");
        Files.writeString(csv, String.join(",", CsvConstants.EXPECTED_HEADER) + "\n");

        FileEventKey key = mock(FileEventKey.class);
        FileEventValue value = mock(FileEventValue.class);
        when(key.getUniqueId()).thenReturn("event-1");
        when(value.getFilePath()).thenReturn(csv.toAbsolutePath().toString());

        Path validated = new FileEventValidator(allowed.toString()).validate(key, value);

        assertEquals(csv.toRealPath(), validated);
    }

    @Test
    void rejectsSymlinkInsideAllowedRootThatPointsOutside() throws Exception {
        Path allowed = Files.createDirectory(tempDir.resolve("allowed"));
        Path outside = Files.createDirectory(tempDir.resolve("outside"));
        Path target = outside.resolve("player_stats.csv");
        Files.writeString(target, String.join(",", CsvConstants.EXPECTED_HEADER) + "\n");
        Path link = allowed.resolve("player_stats.csv");
        Files.createSymbolicLink(link, target);

        FileEventKey key = mock(FileEventKey.class);
        FileEventValue value = mock(FileEventValue.class);
        when(key.getUniqueId()).thenReturn("event-2");
        when(value.getFilePath()).thenReturn(link.toAbsolutePath().toString());

        assertThrows(IllegalArgumentException.class,
                () -> new FileEventValidator(allowed.toString()).validate(key, value));
    }
}
