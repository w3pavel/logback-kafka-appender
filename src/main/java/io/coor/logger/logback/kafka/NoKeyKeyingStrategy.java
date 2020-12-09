package io.coor.logger.logback.kafka;

public class NoKeyKeyingStrategy<Event> implements KeyingStrategy<Event> {

    @Override
    public byte[] createKey(Object o) {
        return null;
    }

}
