package com.spirit.teresa.server.config;

import com.spirit.teresa.config.ProtocolEnum;
import com.spirit.teresa.server.server.TcpRpcServerService;
import com.spirit.teresa.server.server.UdpRpcServerService;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

public class ServerConfigRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata
                .getAnnotationAttributes(ServerConfig.class.getName()));
        String serverAddress = attributes.getString("serverAddress");
        ProtocolEnum protocol = attributes.getEnum("protocol");

        BeanDefinition definition;

        if (protocol.equals(ProtocolEnum.UDP)) {
            definition = BeanDefinitionBuilder.genericBeanDefinition(UdpRpcServerService.class).getBeanDefinition();
        } else {
            definition = BeanDefinitionBuilder.genericBeanDefinition(TcpRpcServerService.class).getBeanDefinition();
        }

        definition.getPropertyValues().add("serverAddress", serverAddress);

        registry.registerBeanDefinition("rpcServer", definition);
    }
}
