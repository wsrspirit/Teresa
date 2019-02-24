package com.tencent.teresa.server;


import com.tencent.teresa.client.DemoService;
import com.tencent.teresa.pb.AddExperienceReq;
import com.tencent.teresa.pb.AddExperienceRsp;
import com.tencent.teresa.packet.ILiveRequest;
import com.tencent.teresa.server.annotation.ServerMethod;
import com.tencent.teresa.server.annotation.ServerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ServerService
public class DemoServiceImpl implements DemoService {
    private static final Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);

    @Override
    @ServerMethod(subcmd = "3")
    public AddExperienceRsp addExp(AddExperienceReq req, ILiveRequest request) {
        AddExperienceRsp rsp = new AddExperienceRsp();
        rsp.setLevel(1);
        rsp.setResult(321313);
        logger.info("req {} rsp {}",req,rsp);
        return rsp;
    }

    public void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @ServerMethod(subcmd = "2")
    public AddExperienceRsp addExp2(AddExperienceReq req, ILiveRequest request) {
        AddExperienceRsp rsp = new AddExperienceRsp();

        rsp.setLevel(2);
        rsp.setResult(44444);
        logger.info("req {} rsp {}",req,rsp);
        return rsp;
    }

    @ServerMethod(subcmd = "1")
    public AddExperienceRsp addExp3(AddExperienceReq req, ILiveRequest request) {
        AddExperienceRsp rsp = new AddExperienceRsp();
        rsp.setLevel(2);
        rsp.setResult(44444);
        logger.info("req {} rsp {}",req,rsp);
        return rsp;
    }
}
