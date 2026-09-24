package com.example.csvstats.consumer;

import com.example.csvstats.service.StatsPlayerPublishService;
import com.example.csvstats.validation.FileEventValidator;
import com.example.csvwatcher.watcher.FileEventKey;
import com.example.csvwatcher.watcher.FileEventValue;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class FileReadyConsumer {
    private final StatsPlayerPublishService publisher;
    private final FileEventValidator validator;

    public FileReadyConsumer(StatsPlayerPublishService publisher, FileEventValidator validator) {
        this.publisher = publisher; this.validator = validator;
    }

    @KafkaListener(topics="${app.kafka.topics.file-ready}", groupId="${spring.kafka.consumer.group-id}")
    public void listen(ConsumerRecord<FileEventKey, FileEventValue> record) {
        FileEventValue value = record.value();
        if (value != null && value.getFileType() != null && !value.getFileType().isBlank()
                && !"STATS_PLAYER".equalsIgnoreCase(value.getFileType())) return;
        var path = validator.validate(record.key(), value);
        publisher.publishFile(record.key().getUniqueId(), path.toString());
    }
}
