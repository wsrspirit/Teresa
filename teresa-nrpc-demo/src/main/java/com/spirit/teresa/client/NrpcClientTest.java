package com.spirit.teresa.client;

import com.spirit.teresa.packet.NrpcPacket;
import com.spirit.teresa.pb.AddExperienceReq;
import com.spirit.teresa.pb.AddExperienceRsp;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class NrpcClientTest {
    private static final Logger logger = LoggerFactory.getLogger(NrpcClientTest.class);
    private static AddExperienceReq req;
    public static void main(String[] args) {
        req = new AddExperienceReq();
        req.setOrderNo("test");
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(TeresaClientConfig.class);
        //使用注解形式实现
        DemoNrpcService demoService = (DemoNrpcService) applicationContext.getBean("demoNrpcService");
        testSync(demoService);
//        testAsync(demoService);
    }

    public static void testSync(DemoNrpcService demoService) {
        NrpcPacket request = new NrpcPacket();
        for (int i = 0; i < 1; i++) {
            AddExperienceRsp addExperienceRsp = demoService.addExp(req,request);
            logger.debug("index {} rsp level {} result {}",i,addExperienceRsp.getLevel(),addExperienceRsp.getResult());
        }
    }

    public static void testAsync(DemoNrpcService demoService) {
//        Flowable<AddExperienceRsp> flowable = demoService.addExpAsync(new AddExperienceReq(),request);
//        flowable.subscribe(addExperienceRsp -> {
//            logger.debug("single rps rsp level {} result {}",addExperienceRsp.getLevel(),addExperienceRsp.getResult());
//        },throwable -> {
//            logger.error("testAsync err: {}",throwable);
//        },() -> {
//            logger.debug("complete");
//        });


        for (int i = 0; i < 5; i++) {
            NrpcPacket request = new NrpcPacket();
            int index = i;
            Flowable<AddExperienceRsp> flowable = demoService.addExpAsync(req,request);
            flowable.subscribe(addExperienceRsp -> {
                logger.debug("index {} rsp level {} result {}",index,addExperienceRsp.getLevel(),addExperienceRsp.getResult());
            },throwable -> {
                logger.error("testAsync err: {}",throwable);
            },() -> {
                logger.debug("{} complete",index);
            });
        }
    }

//    public static void testGetType(DemoNrpcService demoService) {
//        Class<T> entityClass = (Class<T>) ((ParameterizedType) .getGenericSuperclass()).getActualTypeArguments()[0];
//    }
}
