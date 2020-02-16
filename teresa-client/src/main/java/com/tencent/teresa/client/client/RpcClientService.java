package com.tencent.teresa.client.client;

import com.tencent.teresa.client.future.IoPacketFuture;
import com.tencent.teresa.codec.IoPacket;

import java.util.concurrent.Future;

public interface RpcClientService <T_REQ extends IoPacket, T_RSP extends IoPacket> {

    void start();

    T_RSP sync(T_REQ req, long timeout) throws Exception;

    IoPacketFuture<T_RSP> async(final T_REQ req, long timeout) throws Exception;

    void shutdown();
}
