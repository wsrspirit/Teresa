package com.spirit.teresa.worker;

import com.spirit.teresa.codec.IoPacket;
import com.spirit.teresa.handler.DefaultInvocationHandler;
import com.spirit.teresa.handler.InvocationHandler;
import com.spirit.teresa.limiter.DefaultPacketLimiter;
import com.spirit.teresa.limiter.IoPacketLimiter;
import com.spirit.teresa.processor.Processor;
import com.spirit.teresa.utils.ErrorCode;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 这个基类主要负责的是在具体的执行容器执行前的必要操作，如过载保护
 */
public abstract class AbstractWorkerService implements WorkerService{
    /**
     * fixme 这个不是workerService的职责，可以支持传入
     */
    private IoPacketLimiter limiter;
    private static final Logger logger = LoggerFactory.getLogger(AbstractWorkerService.class);
    protected InvocationHandler invocationHandler;
    public AbstractWorkerService() {
        this(new DefaultPacketLimiter());
    }

    public AbstractWorkerService(IoPacketLimiter limiter) {
        this.limiter = limiter;
        this.invocationHandler = new DefaultInvocationHandler();
    }

    public abstract void doDispatch(Channel ch, IoPacket msg, Processor<IoPacket, IoPacket> processor, IoPacketLimiter packetLimiter);

    @Override
    public void dispatch(Channel ch, IoPacket msg, Processor<IoPacket, IoPacket> processor) {
        //todo limiter能不能不向下传递？
        //todo 使用新的流量控制方式，不再向下传递，这样能改写taskHandler的书写方式
        if (limiter.acquire(ch,msg,processor)) {
            doDispatch(ch, msg, processor,limiter);
        } else {
            IoPacket rsp = null;
            try {
                rsp = msg.newResponsePacket(msg, ErrorCode.PACKET_OVER_FLOW.errCode, ErrorCode.PACKET_OVER_FLOW.errMsg, null,null);
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
