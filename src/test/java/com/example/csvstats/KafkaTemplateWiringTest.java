package com.example.csvstats;

import com.fernandez.basketball.contracts.StatsPlayerKey;
import com.fernandez.basketball.contracts.StatsPlayerValue;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.kafka.core.KafkaTemplate;

import static org.assertj.core.api.Assertions.assertThat;

class KafkaTemplateWiringTest {

    private final ApplicationContextRunner contextRunner =
            new ApplicationContextRunner()
                    .withUserConfiguration(CsvStatsPlayerParserApplication.class)
                    .withPropertyValues(
                            "spring.kafka.bootstrap-servers=localhost:9092",
                            "spring.kafka.properties.schema.registry.url=http://localhost:8081",
                            "app.kafka.topics.dlt=file.ready.stats-player.DLT");

    @Test
    void keepsBusinessKafkaTemplateAvailableForStatsPlayerPublisher() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(KafkaTemplate.class);
            assertThat(context.getBean(KafkaTemplate.class)).isNotNull();
        });
    }
}
