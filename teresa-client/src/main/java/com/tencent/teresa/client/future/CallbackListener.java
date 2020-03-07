package com.tencent.teresa.client.future;

public interface CallbackListener<V> {
    void callback(V result);
}
