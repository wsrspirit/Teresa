package com.spirit.teresa.server;


import com.spirit.teresa.client.DemoNrpcService;
import com.spirit.teresa.packet.NrpcPacket;
import com.spirit.teresa.pb.AddExperienceReq;
import com.spirit.teresa.pb.AddExperienceRsp;
import com.spirit.teresa.server.annotation.ServerMethod;
import com.spirit.teresa.server.annotation.ServerService;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ServerService
public class DemoNrpcServiceImpl implements DemoNrpcService {
    private static final Logger logger = LoggerFactory.getLogger(DemoNrpcServiceImpl.class);

    @Override
//    @ServerMethod(subcmd = "addExp")
    public AddExperienceRsp addExp(AddExperienceReq req, NrpcPacket request) {
        AddExperienceRsp rsp = new AddExperienceRsp();
        rsp.setLevel((int) request.getSeq());
        rsp.setResult(44444);

        logger.info("DemoNrpcServiceImpl seq {} req {} rsp {}",request.getSeq(),req,rsp);
        return rsp;
//        throw new RuntimeException("asdasdasdasdasd");
    }

    @Override
    @ServerMethod(subcmd = "addExp")
    public Flowable<AddExperienceRsp> addExpAsync(AddExperienceReq req, NrpcPacket request) {
        AddExperienceRsp rsp = new AddExperienceRsp();
        rsp.setLevel((int) request.getSeq());
        rsp.setResult(44444);

        logger.info("DemoNrpcServiceImpl seq {} req {} rsp {}",request.getSeq(),req,rsp);
        return Flowable.just(rsp);
    }
}
