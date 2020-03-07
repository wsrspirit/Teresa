package com.spirit.teresa.client.spring.config;

import com.spirit.teresa.client.TcpRpcClient;
import com.spirit.teresa.client.UdpRpcClient;
import com.spirit.teresa.config.ProtocolEnum;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

public class ClientConfigRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata
                .getAnnotationAttributes(ClientConfig.class.getName()));
        int connectTimeout = attributes.getNumber("connectTimeout").intValue();
        ProtocolEnum protocol = attributes.getEnum("protocol");
        BeanDefinition definition ;

        if (protocol.equals(ProtocolEnum.UDP)) {
            definition = BeanDefinitionBuilder.genericBeanDefinition(UdpRpcClient.class).getBeanDefinition();
        } else {
            definition = BeanDefinitionBuilder.genericBeanDefinition(TcpRpcClient.class).getBeanDefinition();
        }

        definition.getPropertyValues().add("connectTimeout", connectTimeout);

        registry.registerBeanDefinition("rpcClient", definition);
    }
}
