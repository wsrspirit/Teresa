package com.tencent.teresa.worker;

import co.paralleluniverse.fibers.*;
import com.tencent.teresa.codec.IoPacket;
import com.tencent.teresa.limiter.IoPacketLimiter;
import com.tencent.teresa.processor.Processor;
import com.tencent.teresa.utils.U;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.reactivex.*;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class RxJavaWorkerService extends AbstractWorkerService {
    private Map<ChannelId, Flowable<IoPacket>> flowableMap = new ConcurrentHashMap<>();
    private Map<ChannelId,FlowableEmitter<IoPacket>> flowableEmitterMap = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(RxJavaWorkerService.class);
    private static final int BUFFER_SIZE = 3000;
    private AbstractWorkerService abstractWorkerService;

    public RxJavaWorkerService() {
        this(U.THREAD_WORKER);
    }

    public RxJavaWorkerService(String workerMode) {
        setWorkerMode(workerMode);
        if (workerMode.equals(U.COROUTINE_WORKER)) {
            this.executor = DefaultFiberScheduler.getInstance().getExecutor();
            abstractWorkerService = new CoroutineWorkerService();
        } else {
            abstractWorkerService = new ThreadPoolWorkerService();
        }
    }

    @Override
    public void doDispatch(Channel ch, IoPacket msg, Processor<IoPacket, IoPacket> processor, IoPacketLimiter packetLimiter) {
        Flowable<IoPacket> flowable = flowableMap.get(ch.id());
        FlowableEmitter flowableEmitter = flowableEmitterMap.get(ch.id());

        if (flowable == null) {
            flowable = Flowable.create((FlowableOnSubscribe<IoPacket>) emitter -> {
                flowableEmitterMap.put(ch.id(),emitter);
                emitter.onNext(msg);
            },BackpressureStrategy.DROP)
                    .onBackpressureBuffer(BUFFER_SIZE)
                    .onBackpressureDrop(o -> {
                        logger.error("RxJavaWorkerService backpressure exception {}",o);
                    });

            flowableMap.putIfAbsent(ch.id(),flowable);
            flowable.observeOn(Schedulers.from(abstractWorkerService.getExecutor())).subscribe(o -> {
                logger.info("RxJavaWorkerService doDispatch on next {} threadName {}",o.getSeq(),Thread.currentThread().getName());
                taskHandler.handler(ch, o, processor, packetLimiter);
            },throwable -> {
                logger.error("RxJavaWorkerService doDispatch exception {}",throwable);
            },() -> {
                logger.error("RxJavaWorkerService doDispatch complete {}");
            });
        } else {
            flowableEmitter.onNext(msg);
        }
    }

    @Override
    public Executor getExecutor() {
        return this.abstractWorkerService.getExecutor();
    }

    @Override
    public void setWorkerMode(String workerMode) {

    }


}
