package com.spirit.teresa.limiter;

import com.spirit.teresa.processor.Processor;
import com.spirit.teresa.codec.IoPacket;
import io.netty.channel.Channel;

public interface IoPacketLimiter {
    boolean acquire(Channel channel, IoPacket ioPacket, Processor processor);

    void release(Channel channel, IoPacket ioPacket, Processor processor);
}
