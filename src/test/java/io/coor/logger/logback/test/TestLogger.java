package io.coor.logger.logback.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLogger {
    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(TestLogger.class);

    @Test
    public void test() {
        for (int i = 0; i < 10; i++) {
            logger.info("{}", i);
        }
    }
}
