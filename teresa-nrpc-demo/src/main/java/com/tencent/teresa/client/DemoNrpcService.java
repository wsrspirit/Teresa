package com.tencent.teresa.client;

import com.tencent.teresa.client.spring.annotation.ClientMethod;
import com.tencent.teresa.client.spring.annotation.ClientService;
import com.tencent.teresa.packet.NrpcPacket;
import com.tencent.teresa.pb.AddExperienceReq;
import com.tencent.teresa.pb.AddExperienceRsp;
import io.reactivex.Flowable;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@ClientService(bigCmd = "123",proxy = "nrpcClientProxy",client = "rpcClient")
public interface DemoNrpcService {
    @ClientMethod(subCmd = "addExp",timeout = 80000000)
    AddExperienceRsp addExp(AddExperienceReq req, NrpcPacket request);
    @ClientMethod(subCmd = "addExp",async = true)
    Flowable<AddExperienceRsp> addExpAsync(AddExperienceReq req, NrpcPacket request);
}
