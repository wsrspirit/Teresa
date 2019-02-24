package com.tencent.teresa.worker;

import co.paralleluniverse.fibers.Fiber;
import com.tencent.teresa.codec.IoPacket;
import com.tencent.teresa.limiter.IoPacketLimiter;
import com.tencent.teresa.processor.Processor;
import io.netty.channel.Channel;

import java.util.concurrent.atomic.AtomicInteger;

public class CoroutineWorkerService extends AbstractWorkerService {
    protected static final String FIBER_NAME = "worker-fiber-";
    protected static final AtomicInteger FID = new AtomicInteger(0);

    @Override
    public void doDispatch(Channel ch, IoPacket msg, Processor<IoPacket, IoPacket> processor, IoPacketLimiter packetLimiter) {
        String fiberName = FIBER_NAME + (FID.incrementAndGet() & 0xFFFFFFF);
        new Fiber<Void>(fiberName,() -> {
            taskHandler.handler(ch, msg, processor, packetLimiter);
        }).start();
    }
}
