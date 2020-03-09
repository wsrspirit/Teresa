package com.spirit.teresa.handler;

import co.paralleluniverse.fibers.SuspendExecution;
import com.spirit.teresa.processor.Processor;
import com.spirit.teresa.codec.IoPacket;
import com.spirit.teresa.limiter.IoPacketLimiter;
import io.netty.channel.Channel;

public interface InvocationHandler {
    void handler(Channel ch, IoPacket msg, final Processor<IoPacket, IoPacket> processor
            , IoPacketLimiter packetLimiter) throws SuspendExecution;
}
