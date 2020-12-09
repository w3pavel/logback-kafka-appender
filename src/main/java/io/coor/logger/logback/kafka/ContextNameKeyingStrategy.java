package io.coor.logger.logback.kafka;

import ch.qos.logback.core.Context;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.spi.ContextAwareBase;

import java.nio.ByteBuffer;

public class ContextNameKeyingStrategy<E> extends ContextAwareBase implements KeyingStrategy<E> {

    private byte[] contextNameHash = null;

    @Override
    public void setContext(Context context) {
        super.setContext(context);

        final String contextName = context.getProperty(CoreConstants.CONTEXT_NAME_KEY);
        if (contextName == null) {
            addError("ContextName could not be found in context. ContextNameKeyingStrategy will not work.");
        } else {
            contextNameHash = ByteBuffer.allocate(4).putInt(contextName.hashCode()).array();
        }
    }

    @Override
    public byte[] createKey(E iLoggingEvent) {
        return contextNameHash;
    }
}
