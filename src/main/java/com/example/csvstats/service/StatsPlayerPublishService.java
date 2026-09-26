package com.example.csvstats.service;

import com.example.csvstats.avro.StatsPlayerKey;
import com.example.csvstats.avro.StatsPlayerValue;
import com.example.csvstats.error.NonRetryableCsvException;
import com.example.csvstats.mapper.StatsPlayerMessageMapper;
import com.example.csvstats.parser.StatsPlayerCsvParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class StatsPlayerPublishService {
    static final int CHUNK_SIZE = 500;
    private final StatsPlayerCsvParser parser;
    private final StatsPlayerMessageMapper mapper;
    private final KafkaTemplate<StatsPlayerKey, StatsPlayerValue> kafkaTemplate;
    private final String outputTopic;

    public StatsPlayerPublishService(StatsPlayerCsvParser parser, StatsPlayerMessageMapper mapper,
                                     KafkaTemplate<StatsPlayerKey, StatsPlayerValue> kafkaTemplate,
                                     @Value("${app.kafka.topics.parsed-stats-player}") String outputTopic) {
        this.parser = parser;
        this.mapper = mapper;
        this.kafkaTemplate = kafkaTemplate;
        this.outputTopic = outputTopic;
    }

    public void publishFile(String eventId, String filePath) {
        try {
            parser.parseInChunks(Path.of(filePath), CHUNK_SIZE, players ->
                    players.forEach(player -> kafkaTemplate
                            .send(outputTopic, mapper.toKey(eventId, player), mapper.toValue(eventId, player))
                            .join()));
        } catch (Exception exception) {
            if (containsKafkaFailure(exception)) {
                if (exception instanceof RuntimeException runtimeException) {
                    throw runtimeException;
                }
                throw new RuntimeException(exception);
            }
            throw new NonRetryableCsvException("Unable to parse STATS_PLAYER CSV: " + filePath, exception);
        }
    }

    private boolean containsKafkaFailure(Throwable error) {
        Throwable current = error;
        while (current != null) {
            if (current instanceof org.springframework.kafka.KafkaException
                    || current instanceof org.apache.kafka.common.KafkaException) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }
}
