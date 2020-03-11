package com.spirit.teresa.config;

import com.spirit.teresa.codec.IlivePackageCodec;
import com.spirit.teresa.utils.U;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

public class IliveCodecConfigRegistrar implements ImportBeanDefinitionRegistrar {
    private static final Logger logger = LoggerFactory.getLogger(IliveCodecConfigRegistrar.class);

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata
                .getAnnotationAttributes(IliveCodecConfig.class.getName()));
        boolean isClient = attributes.getBoolean("isClient");
        String serializer = attributes.getString("serializer");


        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(IlivePackageCodec.class);

        switch (serializer) {
            case U.PB_SERIALIZER:
                beanDefinitionBuilder.addPropertyReference("serializer","protoBufSerializer");
                break;
            case U.JSON_SERIALIZER:
                beanDefinitionBuilder.addPropertyReference("serializer","JSON_SERIALIZER");
                break;
            default:
                logger.error("can not find this kind serializer, plz checkout your config annotation");
                break;
        }
        BeanDefinition definition = beanDefinitionBuilder.getBeanDefinition();
        definition.getPropertyValues().add("client", isClient);

        registry.registerBeanDefinition(U.IO_PACKET_CODEC_BEAN_NAME, definition);
    }
}
