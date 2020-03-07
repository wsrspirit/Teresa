package com.spirit.teresa.client.spring.annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class BeanScannerConfigurator implements BeanFactoryPostProcessor, ApplicationContextAware {
    private ApplicationContext applicationContext;
    private static final Logger logger = LoggerFactory.getLogger(BeanScannerConfigurator.class);
    private String scannerPath;

    public BeanScannerConfigurator(String scannerPath) {
        this.scannerPath = scannerPath;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        logger.info("BeanScannerConfigurer postProcessBeanFactory");
        ClientAnnotationScanner clientAnnotationScanner = new ClientAnnotationScanner((BeanDefinitionRegistry) beanFactory);
        clientAnnotationScanner.setResourceLoader(this.applicationContext);
        clientAnnotationScanner.scan(scannerPath);
    }
}
