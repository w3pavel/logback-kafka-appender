package io.coor.logger.logback;

import io.coor.logger.logback.kafka.DeliveryStrategy;
import io.coor.logger.logback.kafka.KeyingStrategy;
import io.coor.logger.logback.kafka.NoKeyKeyingStrategy;
import io.coor.logger.logback.kafka.SyncDeliveryStrategy;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka product config for KafkaAppender
 *
 * @param <E>
 * @author coor
 */
public class KafkaProductConfig<E> {

    /**
     * kafka log topic
     */
    private String topic;

    /**
     * kafka key crate strategy
     */
    private KeyingStrategy<E> keyingStrategy = new NoKeyKeyingStrategy<>();

    /**
     * kafka message send strategy
     */
    private DeliveryStrategy<E> deliveryStrategy = new SyncDeliveryStrategy<>();

    /**
     * product config
     */
    private final Map<String, Object> producerConfig = new HashMap<>();

    public KafkaProductConfig() {
        producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
        producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
    }

    public void addProducerConfig(String keyValue) {
        String[] split = keyValue.split("=", 2);
        if (split.length == 2) {
            this.producerConfig.put(split[0], split[1]);
        }
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public KeyingStrategy<E> getKeyingStrategy() {
        return keyingStrategy;
    }

    public void setKeyingStrategy(KeyingStrategy<E> keyingStrategy) {
        this.keyingStrategy = keyingStrategy;
    }

    public Map<String, Object> getProducerConfig() {
        return producerConfig;
    }

    public DeliveryStrategy<E> getDeliveryStrategy() {
        return deliveryStrategy;
    }

    public void setDeliveryStrategy(DeliveryStrategy<E> deliveryStrategy) {
        this.deliveryStrategy = deliveryStrategy;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("KafkaProductConfig{");
        sb.append("topic='").append(topic).append('\'');
        sb.append(", keyingStrategy=").append(keyingStrategy.getClass().getName());
        sb.append(", deliveryStrategy=").append(deliveryStrategy.getClass().getName());
        sb.append(", producerConfig=").append(producerConfig);
        sb.append('}');
        return sb.toString();
    }
}