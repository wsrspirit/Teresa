package com.spirit.teresa.server.config;

import com.spirit.teresa.config.ProtocolEnum;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 服务端启动的配置项
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(ServerConfigRegistrar.class)
public @interface ServerConfig {
    /**
     * @see ProtocolEnum
     */
    ProtocolEnum protocol();
    String serverAddress();
}
