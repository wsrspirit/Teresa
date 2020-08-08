package com.spirit.teresa.worker;

import com.spirit.teresa.processor.Processor;
import com.spirit.teresa.codec.IoPacket;
import com.spirit.teresa.config.WorkerServiceModeEnum;
import com.spirit.teresa.limiter.IoPacketLimiter;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.reactivex.*;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.*;

public class RxJavaWorkerService extends AbstractWorkerService {
    private Map<ChannelId, Flowable<IoPacket>> flowableMap = new ConcurrentHashMap<>();
    private Map<ChannelId,FlowableEmitter<IoPacket>> flowableEmitterMap = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(RxJavaWorkerService.class);
    private static final int BUFFER_SIZE = 3000;
    private WorkerService workerService;
    private WorkerServiceModeEnum workerServiceModeEnum;

//    public RxJavaWorkerService() {
//        this(U.THREAD_WORKER);workerMode
//    }
//
//    public RxJavaWorkerService(String workerMode) {
//        setWorkerMode(workerMode);
//        if (workerMode.equals(U.COROUTINE_WORKER)) {
//            workerMode = new CoroutineWorkerService();
//        } else {
//            workerMode = new ThreadPoolWorkerService();
//        }
//    }

    public RxJavaWorkerService(WorkerServiceModeEnum workerServiceModeEnum) {
        this.workerServiceModeEnum = workerServiceModeEnum;
        if (workerServiceModeEnum.equals(WorkerServiceModeEnum.RX_COROUTINE_MODE)) {
            workerService = new CoroutineWorkerService();
        } else {
            workerService = new ThreadPoolWorkerService();
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
            flowable.observeOn(Schedulers.from(workerService.getExecutor())).subscribe(o -> {
                logger.info("RxJavaWorkerService doDispatch on next seq {} threadName {}",o.getSeq(),Thread.currentThread().getName());
                logger.info("RxJavaWorkerService doDispatch msg:{}",msg.getSeq());
                processor.process(o, ch);
                packetLimiter.release(ch,o,processor);
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
        return this.workerService.getExecutor();
    }

}
