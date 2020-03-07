package com.spirit.teresa.client.spring.annotation;

import com.spirit.teresa.client.spring.ClientFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.reflect.Method;
import java.util.*;

public class ClientAnnotationScanner extends ClassPathBeanDefinitionScanner {
    private static final Logger logger = LoggerFactory.getLogger(ClientAnnotationScanner.class);
    public ClientAnnotationScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }
    @Override
    public void registerDefaultFilters() {
        this.addIncludeFilter(new AnnotationTypeFilter(ClientService.class));
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        for (BeanDefinitionHolder holder : beanDefinitions) {
            GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();

            Class beanClass = null;
            try {
                beanClass = Class.forName(definition.getBeanClassName());
            } catch (ClassNotFoundException e) {
                logger.error("ClientAnnotationScanner can not class: " + definition.getBeanClassName(),e);
                return beanDefinitions;
            }

            ClientService clientService = (ClientService)beanClass.getAnnotation(ClientService.class);

            Method[] methods = beanClass.getDeclaredMethods();
            Map<String,Map<String,Object>> methodAttrMap = new HashMap<>();
            for (Method m : methods) {
                if (m.isAnnotationPresent(ClientMethod.class)) {
                    ClientMethod clientMethod = m.getAnnotation(ClientMethod.class);
                    Map<String,Object> attrMap = new HashMap<>();
                    attrMap.put("subCmd",clientMethod.subCmd());
                    attrMap.put("timeout",clientMethod.timeout());
                    attrMap.put("async",clientMethod.async());
                    methodAttrMap.put(m.getName(),attrMap);
                }
            }

            definition.getPropertyValues().add("interfaceName", definition.getBeanClassName());
            definition.getPropertyValues().add("bigCmd", clientService.bigCmd());
            definition.getPropertyValues().add("methodAttrMap", methodAttrMap);
            definition.getPropertyValues().add("clientProxy", clientService.proxy());
            definition.getPropertyValues().add("clientInstance", clientService.client());

            definition.setBeanClass(ClientFactoryBean.class);
        }
        return beanDefinitions;
    }

    @Override
    public boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().hasAnnotation(ClientService.class.getName());
    }
}