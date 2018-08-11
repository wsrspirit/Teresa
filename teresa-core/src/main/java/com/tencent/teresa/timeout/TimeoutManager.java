package com.tencent.teresa.timeout;

import com.tencent.teresa.codec.IoPacket;

import java.util.concurrent.Future;

public interface TimeoutManager {
    Future<?> watch(Runnable task, long timeout);
}
