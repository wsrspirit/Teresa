package com.tencent.teresa.config;

import com.tencent.teresa.codec.IlivePackageCodec;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

public class IliveCodecConfigRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata
                .getAnnotationAttributes(IliveCodecConfig.class.getName()));
        boolean isClient = attributes.getBoolean("isClient");
        BeanDefinition definition = BeanDefinitionBuilder.genericBeanDefinition(IlivePackageCodec.class).getBeanDefinition();

        definition.getPropertyValues().add("client", isClient);
        registry.registerBeanDefinition("ioPacketCodec", definition);
    }
}
