package io.coor.logger.logback.kafka;

public interface KeyingStrategy<E> {

    byte[] createKey(E e);

}
