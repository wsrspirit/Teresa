package com.spirit.teresa.client;

import com.spirit.teresa.client.spring.annotation.ClientMethod;
import com.spirit.teresa.client.spring.annotation.ClientService;
import com.spirit.teresa.packet.NrpcPacket;
import com.spirit.teresa.pb.AddExperienceReq;
import com.spirit.teresa.pb.AddExperienceRsp;
import io.reactivex.Flowable;

@ClientService(bigCmd = "123",proxy = "nrpcClientProxy",client = "rpcClient")
public interface DemoNrpcService {
    @ClientMethod(subCmd = "addExp",timeout = 80000000)
    AddExperienceRsp addExp(AddExperienceReq req, NrpcPacket request);
    @ClientMethod(subCmd = "addExp",async = true)
    Flowable<AddExperienceRsp> addExpAsync(AddExperienceReq req, NrpcPacket request);
}
