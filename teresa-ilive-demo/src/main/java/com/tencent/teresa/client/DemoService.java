package com.tencent.teresa.client;

import com.tencent.teresa.client.spring.annotation.ClientMethod;
import com.tencent.teresa.pb.AddExperienceReq;
import com.tencent.teresa.pb.AddExperienceRsp;
import com.tencent.teresa.packet.ILiveRequest;

public interface DemoService {
    @ClientMethod(subCmd = "1")
    AddExperienceRsp addExp(AddExperienceReq req, ILiveRequest request);
}
