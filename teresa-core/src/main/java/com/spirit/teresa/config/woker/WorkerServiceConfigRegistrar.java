package com.spirit.teresa.config.woker;

import com.spirit.teresa.worker.RxJavaWorkerService;
import com.spirit.teresa.worker.ThreadPoolWorkerService;
import com.spirit.teresa.config.WorkerServiceModeEnum;
import com.spirit.teresa.utils.U;
import com.spirit.teresa.worker.CoroutineWorkerService;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

public class WorkerServiceConfigRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata
                .getAnnotationAttributes(WorkerServiceConfig.class.getName()));
        WorkerServiceModeEnum workerServiceModeEnum = attributes.getEnum("workerMode");
        BeanDefinition definition ;

        switch (workerServiceModeEnum) {
            case THREAD:
            default:
                definition = BeanDefinitionBuilder.genericBeanDefinition(ThreadPoolWorkerService.class)
                        .getBeanDefinition();
                break;
            case COROUTINE:
                definition = BeanDefinitionBuilder.genericBeanDefinition(CoroutineWorkerService.class)
                        .getBeanDefinition();
                break;
            case RX_THREAD_MODE:
                definition = BeanDefinitionBuilder.genericBeanDefinition(RxJavaWorkerService.class)
                        .getBeanDefinition();
                ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
                constructorArgumentValues.addGenericArgumentValue(WorkerServiceModeEnum.RX_THREAD_MODE);
                ((AbstractBeanDefinition) definition).setConstructorArgumentValues(constructorArgumentValues);
                break;
            case RX_COROUTINE_MODE:
                definition = BeanDefinitionBuilder.genericBeanDefinition(RxJavaWorkerService.class)
                        .getBeanDefinition();
                ConstructorArgumentValues argumentValues = new ConstructorArgumentValues();
                argumentValues.addGenericArgumentValue(WorkerServiceModeEnum.RX_COROUTINE_MODE);
                ((AbstractBeanDefinition) definition).setConstructorArgumentValues(argumentValues);
                break;
        }

        registry.registerBeanDefinition(U.WORKER_SERVICE_BEAN_NAME, definition);
    }
}
