package com.spirit.teresa.config;

import com.spirit.teresa.codec.NrpcPackageCodec;
import com.spirit.teresa.serializer.JsonSerializer;
import com.spirit.teresa.serializer.ProtobufSerializer;
import com.spirit.teresa.utils.U;
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
        SerializerEnum serializerEnum = attributes.getEnum("serializer");
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(NrpcPackageCodec.class);

        switch (serializerEnum) {
            case PROTOBUF_SERIALIZER:
                BeanDefinition protobufBeanDefinition = BeanDefinitionBuilder.genericBeanDefinition(ProtobufSerializer.class).getBeanDefinition();
                registry.registerBeanDefinition(SerializerEnum.PROTOBUF_SERIALIZER.name(),protobufBeanDefinition);
                beanDefinitionBuilder.addPropertyReference("serializer",SerializerEnum.PROTOBUF_SERIALIZER.name());
                break;
            case JSON_SERIALIZER:
                BeanDefinition jsonBeanDefinition = BeanDefinitionBuilder.genericBeanDefinition(JsonSerializer.class).getBeanDefinition();
                registry.registerBeanDefinition(SerializerEnum.JSON_SERIALIZER.name(),jsonBeanDefinition);
                beanDefinitionBuilder.addPropertyReference("serializer",SerializerEnum.JSON_SERIALIZER.name());
                break;
            default:
                logger.error("can not find this kind serializer, plz checkout your config annotation");
                break;
        }

        BeanDefinition definition = beanDefinitionBuilder.getBeanDefinition();
        registry.registerBeanDefinition(U.IO_PACKET_CODEC_BEAN_NAME, definition);
    }
}
