package com.tencent.teresa.client.spring.config;

import com.tencent.teresa.client.TcpRpcClient;
import com.tencent.teresa.client.UdpRpcClient;
import com.tencent.teresa.utils.U;
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
        String protocol = attributes.getString("protocol");
        BeanDefinition definition ;

        if (U.UDP.equals(protocol)) {
            definition = BeanDefinitionBuilder.genericBeanDefinition(UdpRpcClient.class).getBeanDefinition();
        } else {
            definition = BeanDefinitionBuilder.genericBeanDefinition(TcpRpcClient.class).getBeanDefinition();
        }

        definition.getPropertyValues().add("connectTimeout", connectTimeout);

        registry.registerBeanDefinition("rpcClient", definition);
    }
}
