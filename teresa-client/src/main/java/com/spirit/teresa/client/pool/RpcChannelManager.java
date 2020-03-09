package com.spirit.teresa.client.pool;

import com.spirit.teresa.client.future.IoPacketFuture;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.FixedChannelPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Netty连接池管理者，用于根据addr从池子中获取连接池
 * todo 可以单例化
 */
public class RpcChannelManager extends AbstractChannelPoolMap<InetSocketAddress, FixedChannelPool> {
    private Bootstrap bootstrap;
    private ChannelInitializer<? extends Channel> channelInitializer;
    private ConcurrentHashMap<SocketAddress, ConcurrentHashMap<Long, IoPacketFuture>> channelPacketMap;
    private static final Logger logger = LoggerFactory.getLogger(RpcChannelManager.class);
    private int maxChannelCount ;
    private static final int DEFAULT_CHANNEL_COUNT = 2;
    public RpcChannelManager(Bootstrap bootstrap, ChannelInitializer<? extends Channel> channelInitializer) {
        this(bootstrap,channelInitializer,DEFAULT_CHANNEL_COUNT);
    }

    public RpcChannelManager(Bootstrap bootstrap, ChannelInitializer<? extends Channel> channelInitializer, int maxChannelCount) {
        this.bootstrap = bootstrap;
        this.channelInitializer = channelInitializer;
        this.maxChannelCount = maxChannelCount;
        channelPacketMap = new ConcurrentHashMap<>();
    }

    @Override
    protected FixedChannelPool newPool(InetSocketAddress key) {
        return new FixedChannelPool(bootstrap.remoteAddress(key),new ClientChannelPoolHandler(channelInitializer), maxChannelCount);
    }

    public IoPacketFuture storeSendPackage(SocketAddress address, long id, IoPacketFuture future) {
        IoPacketFuture result;
        logger.debug("storeSendPackage id {} ",id);
        ConcurrentHashMap<Long, IoPacketFuture> map = channelPacketMap.get(address);
        if (map != null) {
            result = map.put(id,future);
        } else {
            ConcurrentHashMap<Long, IoPacketFuture> concurrentHashMap = new ConcurrentHashMap<>(8192, 0.75F, 512);
            result = concurrentHashMap.put(id,future);
            channelPacketMap.putIfAbsent(address,concurrentHashMap);
        }
        return result;
    }

    public IoPacketFuture remove(SocketAddress address, Object id) {
        ConcurrentHashMap<Long, IoPacketFuture> map = channelPacketMap.get(address);
        return map.remove(id);
    }
}
