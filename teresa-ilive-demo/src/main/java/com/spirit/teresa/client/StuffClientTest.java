package com.spirit.teresa.client;

import com.spirit.teresa.pb.AddExperienceReq;
import com.spirit.teresa.pb.AddExperienceRsp;
import com.spirit.teresa.packet.ILiveRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StuffClientTest {
    private static final Logger logger = LoggerFactory.getLogger(StuffClientTest.class);

    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("client-spring.xml");
//        NotInterface notInterface = (NotInterface) applicationContext.getBean("notInterface");
//        ComponentScanBeanDefinitionParser parser = (ComponentScanBeanDefinitionParser)applicationContext.getBean(ComponentScanBeanDefinitionParser.class);

        //使用注解形式实现
        DemoServiceByAnnotation demoServiceByAnnotation = (DemoServiceByAnnotation) applicationContext.getBean("demoServiceByAnnotation");
        ILiveRequest request = new ILiveRequest();
        request.setUid(3333L);
        AddExperienceRsp addExperienceRsp = demoServiceByAnnotation.addExp(new AddExperienceReq(),request);
        logger.debug("rsp level {} result {}",addExperienceRsp.getLevel(),addExperienceRsp.getResult());

//        NotInterface notInterface = (NotInterface) applicationContext.getBean("notInterface");
//        AddExperienceRsp addExperienceRsp1 = notInterface.addExp(more AddExperienceReq());
//        System.release.println(addExperienceRsp1.getLevel() + "---" + addExperienceRsp1.getResult());

        //使用xml方式实现
//        DemoService demoService = (DemoService) applicationContext.getBean("demoService");
//        AddExperienceRsp rsp = demoService.addExp(more AddExperienceReq());
//        System.release.println(rsp.getLevel() + "---" + rsp.getResult());
    }
}
