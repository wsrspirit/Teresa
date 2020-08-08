package com.spirit.teresa.worker;

import co.paralleluniverse.fibers.DefaultFiberScheduler;
import co.paralleluniverse.fibers.FiberScheduler;
import co.paralleluniverse.strands.SuspendableCallable;
import com.spirit.teresa.limiter.IoPacketLimiter;
import com.spirit.teresa.processor.Processor;
import com.spirit.teresa.codec.IoPacket;
import io.netty.channel.Channel;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

public class CoroutineWorkerService extends AbstractWorkerService {
    protected static final String FIBER_NAME = "worker-fiber-";
    protected static final AtomicInteger FID = new AtomicInteger(0);
    private FiberScheduler fiberScheduler;

    public CoroutineWorkerService() {
        super();
        fiberScheduler = DefaultFiberScheduler.getInstance();
    }

    @Override
    public void doDispatch(Channel ch, IoPacket msg, Processor<IoPacket, IoPacket> processor, IoPacketLimiter packetLimiter) {
        fiberScheduler.newFiber((SuspendableCallable<Void>) () -> {
            processor.process(msg, ch);
            packetLimiter.release(ch,msg,processor);
            return null;
        }).start();
    }

    @Override
    public Executor getExecutor() {
        return fiberScheduler.getExecutor();
    }
}
