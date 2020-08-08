package com.spirit.teresa.client.spring.filter;

import com.spirit.teresa.codec.IoPacket;

public interface ClientInvokeFilter {
    void beforeRpcInvoke(IoPacket ioPacket,Object bizRequest);

    void afterRpcInvoke(IoPacket ioPacket,Object bizRequest,IoPacket rsp);
}
