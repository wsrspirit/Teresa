package com.spirit.teresa.limiter;

import com.spirit.teresa.processor.Processor;
import com.spirit.teresa.codec.IoPacket;
import com.spirit.teresa.utils.U;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class DefaultPacketLimiter implements IoPacketLimiter {
    private int totalPermits;
    private int permitsPerSeconds;
    private AtomicInteger permits;
    private static final Logger logger = LoggerFactory.getLogger(DefaultPacketLimiter.class);

    public DefaultPacketLimiter() {
        this(U.DEFAULT_PERMITS);
    }

    public DefaultPacketLimiter(int permits) {
        this.totalPermits = permits;
        this.permits = new AtomicInteger(totalPermits);
    }

    /**
     * 提前消费
     * @param channel
     * @param ioPacket
     * @param processor
     * @return
     */
    @Override
    public boolean acquire(Channel channel, IoPacket ioPacket, Processor processor) {
        int storedLimit = permits.decrementAndGet();
        return storedLimit >= 0;
    }

    @Override
    public void release(Channel channel, IoPacket ioPacket, Processor processor) {
        int storedLimit = permits.incrementAndGet();
        if (storedLimit > totalPermits) {
            logger.error("fetal err!!! storedLimit more than totalPermits");
        }
    }
}
