package com.tencent.teresa.client;

import com.tencent.teresa.packet.NrpcPacket;
import com.tencent.teresa.pb.AddExperienceReq;
import com.tencent.teresa.pb.AddExperienceRsp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class NrpcClientTest {
    private static final Logger logger = LoggerFactory.getLogger(NrpcClientTest.class);

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(TeresaClientConfig.class);
        //使用注解形式实现
        DemoNrpcService demoService = (DemoNrpcService) applicationContext.getBean("demoNrpcService");
        NrpcPacket request = new NrpcPacket();
        for (int i = 0; i < 5; i++) {
            AddExperienceRsp addExperienceRsp = demoService.addExp(new AddExperienceReq(),request);
            logger.debug("index {} rsp level {} result {}",i,addExperienceRsp.getLevel(),addExperienceRsp.getResult());
        }
    }
}
