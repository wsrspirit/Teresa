package com.tencent.teresa.worker;

import co.paralleluniverse.fibers.DefaultFiberScheduler;
import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberScheduler;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.SuspendableCallable;
import com.tencent.teresa.codec.IoPacket;
import com.tencent.teresa.limiter.IoPacketLimiter;
import com.tencent.teresa.processor.Processor;
import com.tencent.teresa.utils.U;
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
            taskHandler.handler(ch, msg, processor, packetLimiter);
            return null;
        }).start();
    }

    @Override
    public Executor getExecutor() {
        return fiberScheduler.getExecutor();
    }

    @Override
    public void setWorkerMode(String workerMode) {
        this.workerMode = U.COROUTINE_WORKER;
    }
}
