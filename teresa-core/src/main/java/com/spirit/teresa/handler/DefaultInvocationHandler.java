package com.spirit.teresa.handler;

import co.paralleluniverse.fibers.SuspendExecution;
import com.spirit.teresa.processor.Processor;
import com.spirit.teresa.codec.IoPacket;
import com.spirit.teresa.limiter.IoPacketLimiter;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultInvocationHandler implements InvocationHandler {
    private static final Logger logger = LoggerFactory.getLogger(DefaultInvocationHandler.class);

    @Override
    public void handler(Channel ch, IoPacket msg, Processor<IoPacket, IoPacket> processor, IoPacketLimiter packetLimiter) throws SuspendExecution {
        //所有的异常处理由processor来搞，这里只是简单的处理
        IoPacket rsp = null;
        rsp = processor.process(msg, ch);
        packetLimiter.release(ch,msg,processor);
        if (rsp != null && msg.isRequest()) {
            ch.writeAndFlush(rsp);
        }

//        try{
//            rsp = processor.process(msg, ch);
//        }
//        catch (Exception ex){
//            logger.error("error acquire processing " + msg.getCmd(), ex);
//            try {
//                rsp = msg.newResponsePacket(msg, ErrorCode.SERVER_INNER_ERR.errCode, ErrorCode.SERVER_INNER_ERR.errMsg, null,null);
//            } catch (Exception e) {
//                logger.error("processor process err and new err response err again!!! {}",e);
//            }
//        } finally {
//            packetLimiter.release(ch,rsp,processor);
//        }
//
//        if (rsp != null) {
//            ch.writeAndFlush(rsp);
//        }
    }
}
