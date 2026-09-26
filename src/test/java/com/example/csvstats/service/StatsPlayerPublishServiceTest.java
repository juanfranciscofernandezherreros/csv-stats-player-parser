package com.example.csvstats.service;

import com.example.csvstats.avro.StatsPlayerKey;
import com.example.csvstats.avro.StatsPlayerValue;
import com.example.csvstats.dto.StatsPlayerDTO;
import com.example.csvstats.mapper.StatsPlayerMessageMapper;
import com.example.csvstats.parser.StatsPlayerCsvParser;
import org.apache.kafka.common.KafkaException;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StatsPlayerPublishServiceTest {

    @Test
    void sendsWholeChunkBeforeWaitingForFirstAck() throws Exception {
        StatsPlayerCsvParser parser = mock(StatsPlayerCsvParser.class);
        StatsPlayerMessageMapper mapper = mock(StatsPlayerMessageMapper.class);
        @SuppressWarnings("unchecked")
        KafkaTemplate<StatsPlayerKey, StatsPlayerValue> kafka = mock(KafkaTemplate.class);
        StatsPlayerDTO first = mock(StatsPlayerDTO.class);
        StatsPlayerDTO second = mock(StatsPlayerDTO.class);
        StatsPlayerKey key = mock(StatsPlayerKey.class);
        StatsPlayerValue value = mock(StatsPlayerValue.class);

        doAnswer(call -> {
            @SuppressWarnings("unchecked")
            java.util.function.Consumer<List<StatsPlayerDTO>> consumer = call.getArgument(2);
            consumer.accept(List.of(first, second));
            return null;
        }).when(parser).parseInChunks(eq(Path.of("/data/player.csv")), eq(500), any());

        when(mapper.toKey(any(), any())).thenReturn(key);
        when(mapper.toValue(any(), any())).thenReturn(value);

        CompletableFuture<Object> firstAck = new CompletableFuture<>();
        CountDownLatch secondSent = new CountDownLatch(1);
        java.util.concurrent.atomic.AtomicInteger sends = new java.util.concurrent.atomic.AtomicInteger();
        when(kafka.send("stats-player.parsed", key, value)).thenAnswer(invocation -> {
            if (sends.incrementAndGet() == 1) return firstAck;
            secondSent.countDown();
            return CompletableFuture.completedFuture(null);
        });

        StatsPlayerPublishService service =
                new StatsPlayerPublishService(parser, mapper, kafka, "stats-player.parsed");
        var executor = Executors.newSingleThreadExecutor();
        try {
            var publish = executor.submit(() -> service.publishFile("e1", "/data/player.csv"));
            assertTrue(secondSent.await(1, TimeUnit.SECONDS));
            firstAck.complete(null);
            publish.get(2, TimeUnit.SECONDS);
        } finally {
            executor.shutdownNow();
        }
    }

    @Test
    void failedAckFailsProcessing() throws Exception {
        StatsPlayerCsvParser parser = mock(StatsPlayerCsvParser.class);
        StatsPlayerMessageMapper mapper = mock(StatsPlayerMessageMapper.class);
        @SuppressWarnings("unchecked")
        KafkaTemplate<StatsPlayerKey, StatsPlayerValue> kafka = mock(KafkaTemplate.class);
        StatsPlayerDTO row = mock(StatsPlayerDTO.class);
        StatsPlayerKey key = mock(StatsPlayerKey.class);
        StatsPlayerValue value = mock(StatsPlayerValue.class);

        doAnswer(call -> {
            @SuppressWarnings("unchecked")
            java.util.function.Consumer<List<StatsPlayerDTO>> consumer = call.getArgument(2);
            consumer.accept(List.of(row));
            return null;
        }).when(parser).parseInChunks(any(Path.class), eq(500), any());

        when(mapper.toKey(any(), any())).thenReturn(key);
        when(mapper.toValue(any(), any())).thenReturn(value);
        when(kafka.send(any(), any(), any()))
                .thenReturn((CompletableFuture) CompletableFuture.failedFuture(new KafkaException("down")));

        StatsPlayerPublishService service =
                new StatsPlayerPublishService(parser, mapper, kafka, "stats-player.parsed");
        assertThrows(RuntimeException.class, () -> service.publishFile("e1", "/data/player.csv"));
    }
}
