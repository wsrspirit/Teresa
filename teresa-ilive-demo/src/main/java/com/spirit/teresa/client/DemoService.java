package com.spirit.teresa.client;

import com.spirit.teresa.client.spring.annotation.ClientMethod;
import com.spirit.teresa.pb.AddExperienceReq;
import com.spirit.teresa.pb.AddExperienceRsp;
import com.spirit.teresa.packet.ILiveRequest;

public interface DemoService {
    @ClientMethod(subCmd = "1")
    AddExperienceRsp addExp(AddExperienceReq req, ILiveRequest request);
}
