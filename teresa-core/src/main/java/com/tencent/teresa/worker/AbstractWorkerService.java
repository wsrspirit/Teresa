package com.tencent.teresa.worker;

import com.tencent.teresa.codec.IoPacket;
import com.tencent.teresa.handler.DefaultTaskHandler;
import com.tencent.teresa.handler.TaskHandler;
import com.tencent.teresa.limiter.DefaultPacketLimiter;
import com.tencent.teresa.limiter.IoPacketLimiter;
import com.tencent.teresa.processor.Processor;
import com.tencent.teresa.utils.CommonErr;
import com.tencent.teresa.utils.U;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

/**
 * 这个基类主要负责的是在具体的执行容器执行前的必要操作，如过载保护
 */
public abstract class AbstractWorkerService implements WorkerService{
    private IoPacketLimiter limiter;
    private static final Logger logger = LoggerFactory.getLogger(AbstractWorkerService.class);
    protected TaskHandler taskHandler;
    protected Executor executor;
    protected String workerMode;

    public AbstractWorkerService() {
        this(new DefaultPacketLimiter());
    }

    public AbstractWorkerService(IoPacketLimiter limiter) {
        this.limiter = limiter;
        this.taskHandler = new DefaultTaskHandler();
    }

    public abstract void doDispatch(Channel ch, IoPacket msg, Processor<IoPacket, IoPacket> processor, IoPacketLimiter packetLimiter);

    public abstract Executor getExecutor();

    public abstract void setWorkerMode(String workerMode);

    @Override
    public void dispatch(Channel ch, IoPacket msg, Processor<IoPacket, IoPacket> processor) {
        if (limiter.acquire(ch,msg,processor)) {
            doDispatch(ch, msg, processor,limiter);
        } else {
            IoPacket rsp = null;
            try {
                rsp = msg.newResponsePacket(msg, CommonErr.PACKET_OVER_FLOW.errCode, CommonErr.PACKET_OVER_FLOW.errMsg, null,null);
            } catch (Exception e) {
                logger.error("server flow over limit, but new response err!!! {}",e);
            }
            if (rsp != null) {
                ch.writeAndFlush(rsp);
            }
            limiter.release(ch,rsp,processor);
        }
    }
}
