package com.spirit.teresa.client.future;

import com.spirit.teresa.codec.IoPacket;

public interface IoPacketFuture<V> {
    IoPacket getIoPacket();
    void setResult(V result);
    V get() throws Exception;
    boolean isDone();

    /**
     * 设置future的回调函数
     * @param listener
     */
    void setCallbackListener(CallbackListener<V> listener);

    /**
     * 激活future的回调函数
     */
    void fireCallbackListener();
}
