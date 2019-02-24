package com.tencent.teresa.config;

import com.tencent.teresa.codec.NrpcPackageCodec;
import com.tencent.teresa.serializer.JsonSerializer;
import com.tencent.teresa.serializer.ProtobufSerializer;
import com.tencent.teresa.utils.U;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

public class NrpcCodecConfigRegistrar implements ImportBeanDefinitionRegistrar {
    private static final Logger logger = LoggerFactory.getLogger(NrpcCodecConfigRegistrar.class);

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata
                .getAnnotationAttributes(NrpcCodecConfig.class.getName()));
        String serializer = attributes.getString("serializer");

        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(NrpcPackageCodec.class);

        switch (serializer) {
            case U.PB_SERIALIZER:
                beanDefinitionBuilder.addPropertyReference("serializer","protoBufSerializer");
                break;
            case U.JSON_SERIALIZER:
                beanDefinitionBuilder.addPropertyReference("serializer","jsonSerializer");
                break;
            default:
                logger.error("can not find this kind serializer, plz checkout your config annotation");
                break;
        }

        BeanDefinition definition = beanDefinitionBuilder.getBeanDefinition();
        registry.registerBeanDefinition("ioPacketCodec", definition);
    }
}
