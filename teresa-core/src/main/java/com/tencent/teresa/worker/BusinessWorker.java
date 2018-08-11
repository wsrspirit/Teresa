package com.tencent.teresa.worker;

import com.tencent.teresa.codec.IoPacket;
import com.tencent.teresa.limiter.IoPacketLimiter;
import com.tencent.teresa.processor.Processor;
import com.tencent.teresa.utils.CommonErr;
import com.tencent.teresa.utils.U;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BusinessWorker implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(BusinessWorker.class);
    private final Channel ch;
    private final IoPacket msg;
    private final Processor<IoPacket, IoPacket> processor;
    private IoPacketLimiter packetLimiter;

    public BusinessWorker(final Channel ch, final IoPacket msg, final Processor<IoPacket, IoPacket> processor, IoPacketLimiter packetLimiter) {
        this.ch = ch;
        this.msg = msg;
        this.processor = processor;
        this.packetLimiter = packetLimiter;
    }

    @Override
    public void run() {
        IoPacket rsp = null;
        try{
            rsp = processor.process(msg, ch);
        }
        catch (Exception ex){
            ThreadPoolWorkerService.logger.error("error acquire processing " + msg.getCmd(), ex);
            try {
                rsp = msg.newResponsePacket(msg, CommonErr.SERVER_INNER_ERR.errCode, CommonErr.SERVER_INNER_ERR.errMsg, null,null);
            } catch (Exception e) {
                logger.error("processor process err and new err response err again!!! {}",e);
            }
        } finally {
            packetLimiter.release(ch,rsp,processor);
        }

        if (rsp != null) {
            ch.writeAndFlush(rsp);
        }

    }
}
