package com.tencent.teresa.limiter;

import com.tencent.teresa.codec.IoPacket;
import com.tencent.teresa.processor.Processor;
import com.tencent.teresa.utils.U;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class DefaultPacketLimiter implements IoPacketLimiter {
    private int permitsPerSecond;
    private AtomicInteger permits;
    private static final Logger logger = LoggerFactory.getLogger(DefaultPacketLimiter.class);

    public DefaultPacketLimiter() {
        this(U.DEFAULT_PERMITS);
    }

    public DefaultPacketLimiter(int permits) {
        this.permitsPerSecond = permits;
        this.permits = new AtomicInteger(permitsPerSecond);
    }

    @Override
    public boolean acquire(Channel channel, IoPacket ioPacket, Processor processor) {
        int storedLimit = permits.get();
        if (storedLimit <= 0) {
            return false;
        }
        permits.decrementAndGet();
        return true;
    }

    @Override
    public void release(Channel channel, IoPacket ioPacket, Processor processor) {
        int storedLimit = permits.incrementAndGet();
        if (storedLimit > permitsPerSecond) {
            logger.error("fetal err!!! storedLimit more than permitsPerSecond");
        }
    }
}
