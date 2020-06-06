package com.spirit.teresa.client.spring.config;

import com.spirit.teresa.config.ProtocolEnum;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 客户端启动的配置项
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(ClientConfigRegistrar.class)
public @interface ClientConfig {
    int connectTimeout() default 10000;
    ProtocolEnum protocol();
}
