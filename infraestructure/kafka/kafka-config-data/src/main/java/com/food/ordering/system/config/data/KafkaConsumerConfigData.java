package com.food.ordering.system.config.data;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka-consumer-config")
public class KafkaConsumerConfigData {

    private String keyDeserializer;
    private String valueDeserializer;
    private String autoOffsetReset;
    private String specificAvoReaderKey;
    private String specificAvoReader;
    private Boolean batchListener;
    private Boolean autoStartUp;
    private Integer concurrencyLevel;
    private Integer sessionTimeOutMs;
    private Integer heartbeatIntervalMs;
    private Integer maxPollIntervalMs;
    private Long pollTimeoutMs;
    private Integer maxPollRecords;
    private Integer maxPartitionFetchBytesDefault;
    private Integer maxPartitionFetchBytesBootsFactor;
}
