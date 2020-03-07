package com.spirit.teresa.handler;

import co.paralleluniverse.fibers.SuspendExecution;
import com.spirit.teresa.processor.Processor;
import com.spirit.teresa.codec.IoPacket;
import com.spirit.teresa.limiter.IoPacketLimiter;
import com.spirit.teresa.utils.CommonErr;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultTaskHandler implements TaskHandler {
    private static final Logger logger = LoggerFactory.getLogger(DefaultTaskHandler.class);

    @Override
    public void handler(Channel ch, IoPacket msg, Processor<IoPacket, IoPacket> processor, IoPacketLimiter packetLimiter) throws SuspendExecution {
        IoPacket rsp = null;
        try{
            rsp = processor.process(msg, ch);
        }
        catch (Exception ex){
            logger.error("error acquire processing " + msg.getCmd(), ex);
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
