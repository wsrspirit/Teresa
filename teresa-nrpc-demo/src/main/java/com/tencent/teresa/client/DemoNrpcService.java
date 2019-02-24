package com.tencent.teresa.client;

import com.tencent.teresa.client.spring.annotation.ClientMethod;
import com.tencent.teresa.client.spring.annotation.ClientService;
import com.tencent.teresa.packet.NrpcPacket;
import com.tencent.teresa.pb.AddExperienceReq;
import com.tencent.teresa.pb.AddExperienceRsp;

@ClientService(bigCmd = "123",proxy = "nrpcClientProxy",client = "rpcClient")
public interface DemoNrpcService {
    @ClientMethod(subCmd = "addExp",timeout = 100000)
    AddExperienceRsp addExp(AddExperienceReq req, NrpcPacket request);
}
