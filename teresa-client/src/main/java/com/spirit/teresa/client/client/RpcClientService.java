package com.spirit.teresa.client.client;

import com.spirit.teresa.client.future.IoPacketFuture;
import com.spirit.teresa.codec.IoPacket;

public interface RpcClientService <T_REQ extends IoPacket, T_RSP extends IoPacket> {

    void start();

    T_RSP sync(T_REQ req, long timeout) throws Exception;

    IoPacketFuture<T_RSP> async(final T_REQ req, long timeout) throws Exception;

    void shutdown();
}
