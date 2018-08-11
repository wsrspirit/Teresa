package com.tencent.teresa.worker;

import com.tencent.teresa.codec.IoPacket;
import com.tencent.teresa.limiter.DefaultPacketLimiter;
import com.tencent.teresa.limiter.IoPacketLimiter;
import com.tencent.teresa.processor.Processor;
import com.tencent.teresa.utils.CommonErr;
import com.tencent.teresa.utils.U;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractWorkerService implements WorkerService{
    private IoPacketLimiter limiter;
    private static final Logger logger = LoggerFactory.getLogger(AbstractWorkerService.class);

    public AbstractWorkerService() {
        limiter = new DefaultPacketLimiter();
    }

    public AbstractWorkerService(IoPacketLimiter limiter) {
        this.limiter = limiter;
    }

    public abstract void doDispatch(Channel ch, IoPacket msg, Processor<IoPacket, IoPacket> processor, IoPacketLimiter packetLimiter);

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
