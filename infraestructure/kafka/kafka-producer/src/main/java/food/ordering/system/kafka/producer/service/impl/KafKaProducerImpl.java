package food.ordering.system.kafka.producer.service.impl;

import food.ordering.system.kafka.producer.exception.KafkaProducerException;
import food.ordering.system.kafka.producer.service.KafKaProducer;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;


import java.io.Serializable;

@Slf4j
@Component
public class KafKaProducerImpl<K extends Serializable, V extends SpecificRecordBase > implements KafKaProducer<K, V> {

    private final KafkaTemplate<K, V> kafkaTemplate;

    public KafKaProducerImpl(KafkaTemplate<K, V> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(String topicName, K key, V message, ListenableFutureCallback<SendResult<K, V>> callback) {
        log.info("Sending message= {} to topic= {}", message, topicName);
        try {
            ListenableFuture<SendResult<K, V>> kafkaResultFuture = (ListenableFuture<SendResult<K, V>>) kafkaTemplate.send(topicName, key, message);
            kafkaResultFuture.addCallback(callback);
        }catch (KafkaProducerException e) {
            log.error("Error on kafka producer with key: {}, message: {} and exception: {}", key, message, e.getMessage());
            throw new KafkaProducerException("Error on kafka producer with key: "+key+" message: "+message);
        }
    }

    @PreDestroy
    public void close() {
        if(kafkaTemplate == null) {
            log.info("Closing kafka producer !");
            kafkaTemplate.destroy();
        }
    }
}
