package com.spirit.teresa.client;

import com.spirit.teresa.client.spring.annotation.ClientMethod;
import com.spirit.teresa.client.spring.annotation.ClientService;
import com.spirit.teresa.packet.ILiveRequest;
import com.spirit.teresa.pb.AddExperienceReq;
import com.spirit.teresa.pb.AddExperienceRsp;

@ClientService(bigCmd = "123",proxy = "iliveClientProxy",client = "rpcClient")
public interface DemoServiceByAnnotation {
    @ClientMethod(subCmd = "1",timeout = 100000)
    AddExperienceRsp addExp(AddExperienceReq req, ILiveRequest request);
}
