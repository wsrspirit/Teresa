package com.spirit.teresa.client.spring.config;

import com.spirit.teresa.client.TcpRpcClient;
import com.spirit.teresa.client.UdpRpcClient;
import com.spirit.teresa.codec.IoPacketCodec;
import com.spirit.teresa.config.ProtocolEnum;
import com.spirit.teresa.route.RouterService;
import com.spirit.teresa.timeout.DefaultTimeoutManager;
import com.spirit.teresa.timeout.TimeoutManager;
import com.spirit.teresa.utils.U;
import com.spirit.teresa.worker.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
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

        BeanDefinitionBuilder beanDefinitionBuilder;
        if (protocol.equals(ProtocolEnum.UDP)) {
            beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(UdpRpcClient.class);
        } else {
            beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(TcpRpcClient.class);
        }

        beanDefinitionBuilder.addPropertyReference(U.ROUTER_SERVICE_BEAN_NAME,U.ROUTER_SERVICE_BEAN_NAME);
        beanDefinitionBuilder.addPropertyReference(U.IO_PACKET_CODEC_BEAN_NAME,U.IO_PACKET_CODEC_BEAN_NAME);
        beanDefinitionBuilder.addPropertyReference(U.WORKER_SERVICE_BEAN_NAME, U.WORKER_SERVICE_BEAN_NAME);
        definition = beanDefinitionBuilder.getBeanDefinition();
        definition.getPropertyValues().add("connectTimeout", connectTimeout);

        registry.registerBeanDefinition(U.RPC_CLIENT_BEAN_NAME, definition);
    }
}
