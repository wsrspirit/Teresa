package com.tencent.teresa.handler;

import co.paralleluniverse.fibers.SuspendExecution;
import com.tencent.teresa.codec.IoPacket;
import com.tencent.teresa.limiter.IoPacketLimiter;
import com.tencent.teresa.processor.Processor;
import io.netty.channel.Channel;

public interface TaskHandler {
    void handler(Channel ch, IoPacket msg, final Processor<IoPacket, IoPacket> processor
            , IoPacketLimiter packetLimiter) throws SuspendExecution;
}
