package com.tencent.teresa.server.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(ServerConfigRegistrar.class)
public @interface ServerConfig {
    String protocol();
    String serverAddress();
}
