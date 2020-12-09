package io.coor.logger.logback;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

import java.util.Map;

/**
 * Kafka LazyProduct
 *
 * @author Pavel
 */
public class LazyProducer {

    private volatile Producer<byte[], byte[]> producer;

    private final Map<String, Object> producerConfig;

    public LazyProducer(Map<String, Object> producerConfig) {
        this.producerConfig = producerConfig;
    }

    public Producer<byte[], byte[]> get() {
        Producer<byte[], byte[]> result = this.producer;
        if (result == null) {
            synchronized (this) {
                result = this.producer;
                if (result == null) {
                    this.producer = result = new KafkaProducer<>(producerConfig);
                }
            }
        }

        return result;
    }

    public boolean isInitialized() {
        return producer != null;
    }

    public void close() {
        if (producer != null) {
            producer.close();
            producer = null;
        }
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("LazyProducer{");
        sb.append("producerConfig=").append(producerConfig);
        sb.append('}');
        return sb.toString();
    }
}