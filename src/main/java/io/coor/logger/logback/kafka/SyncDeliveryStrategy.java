package io.coor.logger.logback.kafka;

import io.coor.logger.logback.LazyProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SyncDeliveryStrategy<Event> implements DeliveryStrategy<Event> {

    @Override
    public void send(LazyProducer lazyProducer,
                     Event e,
                     ProducerRecord<byte[], byte[]> record,
                     FailedDeliveryCallback<Event> callback) {
        Future<RecordMetadata> future = lazyProducer.get().send(record);
        try {
            future.get();
        } catch (InterruptedException | ExecutionException exception) {
            callback.onFailedDelivery(e, exception);
        }
    }
}
