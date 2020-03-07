package com.spirit.teresa.client.annotation;

import com.spirit.teresa.client.DemoServiceByAnnotation;
import com.spirit.teresa.packet.ILiveRequest;
import com.spirit.teresa.pb.AddExperienceReq;
import com.spirit.teresa.pb.AddExperienceRsp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AnnotationClientTest {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationClientTest.class);

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(TeresaClientConfig.class);
        //使用注解形式实现
        DemoServiceByAnnotation demoService = (DemoServiceByAnnotation) applicationContext.getBean("demoServiceByAnnotation");
        ILiveRequest request = new ILiveRequest();
        request.setUid(3333L);
        for (int i = 0; i < 5; i++) {
            AddExperienceRsp addExperienceRsp = demoService.addExp(new AddExperienceReq(),request);
            logger.debug("index {} rsp level {} result {}",i,addExperienceRsp.getLevel(),addExperienceRsp.getResult());
        }
    }
}
