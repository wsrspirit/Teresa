package com.tencent.teresa.client.spring.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(ClientConfigRegistrar.class)
public @interface ClientConfig {
    int connectTimeout() default 10000;
    String protocol();
}
