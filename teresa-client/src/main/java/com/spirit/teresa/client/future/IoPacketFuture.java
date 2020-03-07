package com.spirit.teresa.client.future;

import com.spirit.teresa.codec.IoPacket;

public interface IoPacketFuture<V> {
    IoPacket getIoPacket();
    void setResult(V result);
    V get() throws Exception;
    boolean isDone();
    void setCallbackListener(CallbackListener<V> listener);
    void callback();
}
