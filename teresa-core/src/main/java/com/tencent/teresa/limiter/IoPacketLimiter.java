package com.tencent.teresa.limiter;

import com.tencent.teresa.codec.IoPacket;
import com.tencent.teresa.processor.Processor;
import io.netty.channel.Channel;

public interface IoPacketLimiter {
    boolean acquire(Channel channel, IoPacket ioPacket, Processor processor);

    void release(Channel channel, IoPacket ioPacket, Processor processor);
}
