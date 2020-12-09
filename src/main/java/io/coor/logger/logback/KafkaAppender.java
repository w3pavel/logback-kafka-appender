package io.coor.logger.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.spi.AppenderAttachable;
import ch.qos.logback.core.spi.AppenderAttachableImpl;
import io.coor.logger.logback.kafka.FailedDeliveryCallback;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;

import java.util.Iterator;

/**
 * Logback Kafka appender.
 *
 * @param <Event>
 */
public class KafkaAppender<Event extends ILoggingEvent> extends UnsynchronizedAppenderBase<Event>
        implements AppenderAttachable<Event> {

    private final static String HOOK_THREAD_NAME = "ShutdownHook-Thread";

    /**
     * appender-ref appender
     */
    private final AppenderAttachableImpl<Event> aai = new AppenderAttachableImpl<>();

    /**
     * convert event to byte[]
     */
    protected Encoder<ILoggingEvent> encoder;

    /**
     * kafka product configuration
     */
    protected KafkaProductConfig<Event> kafkaProduct;

    private volatile LazyProducer lazyProducer;

    private final FailedDeliveryCallback<Event> failedDeliveryCallback = (evt, throwable) -> aai.appendLoopOnAppenders(evt);

    public KafkaAppender() {
        Runtime.getRuntime().addShutdownHook(new Thread(KafkaAppender.this::stop, HOOK_THREAD_NAME));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void start() {
        lazyProducer = new LazyProducer(kafkaProduct.getProducerConfig());
        ILoggingEvent loggingEvent = new LoggingEvent(ch.qos.logback.classic.Logger.class.getName(),
                new LoggerContext().getLogger(KafkaAppender.class),
                Level.INFO,
                "logback-kafka-appender started", null, null);
        ProducerRecord<byte[], byte[]> producerRecord = new ProducerRecord<>(
                kafkaProduct.getTopic(),
                kafkaProduct.getKeyingStrategy().createKey((Event) loggingEvent),
                this.encoder.encode(loggingEvent));
        kafkaProduct.getDeliveryStrategy().send(lazyProducer, (Event) loggingEvent, producerRecord, failedDeliveryCallback);
        super.start();
    }


    @Override
    public void stop() {
        super.stop();
        if (lazyProducer != null && lazyProducer.isInitialized()) {
            try {
                lazyProducer.close();
            } catch (KafkaException e) {
                this.addWarn("Failed to shut down kafka producer: " + e.getMessage(), e);
            }
            lazyProducer = null;
        }
    }

    @Override
    public void doAppend(Event event) {
        aai.appendLoopOnAppenders(event);
        super.doAppend(event);
    }

    @Override
    protected void append(Event eventObject) {
        final byte[] key = kafkaProduct.getKeyingStrategy().createKey(eventObject);
        final byte[] value = encoder.encode(eventObject);

        ProducerRecord<byte[], byte[]> producerRecord
                = new ProducerRecord<>(kafkaProduct.getTopic(), null, eventObject.getTimeStamp(), key, value);
        kafkaProduct.getDeliveryStrategy().send(lazyProducer, eventObject, producerRecord, failedDeliveryCallback);
    }

    public void setEncoder(Encoder<ILoggingEvent> encoder) {
        this.encoder = encoder;
    }

    public void setKafkaProduct(KafkaProductConfig<Event> kafkaProduct) {
        this.kafkaProduct = kafkaProduct;
    }

    @Override
    public void addAppender(Appender<Event> newAppender) {
        aai.addAppender(newAppender);
    }

    @Override
    public Iterator<Appender<Event>> iteratorForAppenders() {
        return aai.iteratorForAppenders();
    }

    @Override
    public Appender<Event> getAppender(String name) {
        return aai.getAppender(name);
    }

    @Override
    public boolean isAttached(Appender<Event> appender) {
        return aai.isAttached(appender);
    }

    @Override
    public void detachAndStopAllAppenders() {
        aai.detachAndStopAllAppenders();
    }

    @Override
    public boolean detachAppender(Appender<Event> appender) {
        return aai.detachAppender(appender);
    }

    @Override
    public boolean detachAppender(String name) {
        return aai.detachAppender(name);
    }


}
