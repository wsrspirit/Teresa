package com.spirit.teresa.config.woker;

import com.spirit.teresa.config.WorkerServiceModeEnum;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(WorkerServiceConfigRegistrar.class)
public @interface WorkerServiceConfig {
    WorkerServiceModeEnum workerMode();
}
