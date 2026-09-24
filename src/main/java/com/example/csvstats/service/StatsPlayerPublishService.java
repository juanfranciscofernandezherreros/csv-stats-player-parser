package com.example.csvstats.service;

import com.example.csvstats.avro.StatsPlayerKey;
import com.example.csvstats.avro.StatsPlayerValue;
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
        this.parser = parser; this.mapper = mapper; this.kafkaTemplate = kafkaTemplate; this.outputTopic = outputTopic;
    }

    public void publishFile(String eventId, String filePath) {
        try {
            parser.parseInChunks(Path.of(filePath), CHUNK_SIZE, players ->
                players.forEach(p -> kafkaTemplate.send(outputTopic, mapper.toKey(eventId,p), mapper.toValue(eventId,p)).join()));
        } catch (Exception e) {
            throw new IllegalStateException("Error al publicar STATS_PLAYER parseado: " + filePath, e);
        }
    }
}
