package com.spirit.teresa.client.spring.annotation;

import com.spirit.teresa.client.spring.ClientFactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

public class ClientServiceRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata
                .getAnnotationAttributes(ClientService.class.getName()));
        String bigCmd = attributes.getString("bigCmd");
        String proxy = attributes.getString("proxy");
        String client = attributes.getString("client");

        BeanDefinition annotationProcessor = BeanDefinitionBuilder.genericBeanDefinition(ClientFactoryBean.class).getBeanDefinition();
    }
}
