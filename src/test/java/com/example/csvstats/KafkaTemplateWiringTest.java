package com.example.csvstats;

import com.example.csvstats.service.StatsPlayerPublishService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "spring.kafka.bootstrap-servers=localhost:9092",
        "spring.kafka.properties.schema.registry.url=http://localhost:8081",
        "app.kafka.topics.dlt=file.ready.stats-player.DLT",
        "app.kafka.topics.parsed-stats-player=stats-player.parsed"
})
class KafkaTemplateWiringTest {

    @Autowired
    private StatsPlayerPublishService publisher;

    @Test
    void applicationContextProvidesStatsPlayerPublisher() {
        assertThat(publisher).isNotNull();
    }
}
