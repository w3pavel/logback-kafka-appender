package io.coor.logger.logback.kafka;

import io.coor.logger.logback.LazyProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class AnsyncDeliveryStrategy<Event> implements DeliveryStrategy<Event> {

    @Override
    public void send(LazyProducer lazyProducer, Event e,
                     ProducerRecord<byte[], byte[]> record,
                     FailedDeliveryCallback<Event> callback) {
        try {
            lazyProducer.get().send(record, (metadata, exception) -> {
                if (exception != null) {
                    callback.onFailedDelivery(e, exception);
                }
            });
        } catch (Exception exception) {
            callback.onFailedDelivery(e, exception);
        }
    }
}
