package io.coor.logger.logback.kafka;

public interface FailedDeliveryCallback<Event> {

    void onFailedDelivery(Event evt, Throwable throwable);

}
