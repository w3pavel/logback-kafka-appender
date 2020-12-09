package io.coor.logger.logback.kafka;

import io.coor.logger.logback.LazyProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public interface DeliveryStrategy<Event> {

    void send(LazyProducer lazyProducer,
              Event e,
              ProducerRecord<byte[], byte[]> record,
              FailedDeliveryCallback<Event> callback);

}
