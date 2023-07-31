package com.food.ordering.system.config.data;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka-config")
public class KafkaConfigData {

    private String bootstrapServer;
    private String schemaRegistryUrlKy;
    private String schemaRegistryUrl;
    private String numOfPartitions;
    private Short replicationFactor;
}
