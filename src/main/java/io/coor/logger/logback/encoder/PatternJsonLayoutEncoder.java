package io.coor.logger.logback.encoder;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.PatternLayoutEncoderBase;

import java.util.Map;

public class PatternJsonLayoutEncoder extends PatternLayoutEncoderBase<ILoggingEvent> {

    @Override
    public void start() {
        this.setPattern(this.getPattern()
                .replace("\n", "")
                .replace("\r\n", "")
                .replaceAll("^\\{ *\"", "\\{\"")
                .replaceAll("\", *\"", "\",\"")
                .replaceAll("\" *}$", "\"}"));

        PatternLayout patternLayout = new PatternLayout();
        patternLayout.setContext(context);
        patternLayout.setPattern(getPattern());
        patternLayout.setOutputPatternAsHeader(outputPatternAsHeader);
        Map<String, String> converterMapper = patternLayout.getDefaultConverterMap();
        converterMapper.put("m", MessageConverter.class.getName());
        converterMapper.put("msg", MessageConverter.class.getName());
        converterMapper.put("message", MessageConverter.class.getName());
        patternLayout.start();
        this.layout = patternLayout;
        super.start();
    }

}
