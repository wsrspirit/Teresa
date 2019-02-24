package com.tencent.teresa.server;


import com.tencent.teresa.client.DemoNrpcService;
import com.tencent.teresa.packet.NrpcPacket;
import com.tencent.teresa.pb.AddExperienceReq;
import com.tencent.teresa.pb.AddExperienceRsp;
import com.tencent.teresa.server.annotation.ServerMethod;
import com.tencent.teresa.server.annotation.ServerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ServerService
public class DemoNrpcServiceImpl implements DemoNrpcService {
    private static final Logger logger = LoggerFactory.getLogger(DemoNrpcServiceImpl.class);

    @Override
    @ServerMethod(subcmd = "addExp")
    public AddExperienceRsp addExp(AddExperienceReq req, NrpcPacket request) {
        AddExperienceRsp rsp = new AddExperienceRsp();
        rsp.setLevel(2);
        rsp.setResult(44444);
        logger.info("req {} rsp {}",req,rsp);
        return rsp;
    }
}
