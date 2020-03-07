package com.spirit.teresa.server.annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ServerAnnotationTest {
    private static final Logger logger = LoggerFactory.getLogger(ServerAnnotationTest.class);
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TeresaServerConfig.class);
        logger.info("ServerAnnotationTest context load complete");
    }
}
