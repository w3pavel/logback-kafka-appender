package io.coor.logger.logback.encoder;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class MessageConverter extends ClassicConverter {

    public String convert(ILoggingEvent event) {
        return event.getFormattedMessage().replace("\"", "\\\"");
    }

}