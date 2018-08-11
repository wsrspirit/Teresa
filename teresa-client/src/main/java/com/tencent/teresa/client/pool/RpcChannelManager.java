package com.tencent.teresa.client.pool;

import com.tencent.teresa.codec.IoPacket;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.FixedChannelPool;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;

public class RpcChannelManager extends AbstractChannelPoolMap<InetSocketAddress, FixedChannelPool> {
    private Bootstrap bootstrap;
    private ChannelInitializer<? extends Channel> channelInitializer;
    private ConcurrentHashMap<SocketAddress, ConcurrentHashMap<Long, IoPacket>> channelPacketMap;

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

    public IoPacket put(SocketAddress address,long id,IoPacket future) {
        IoPacket result;
        ConcurrentHashMap<Long, IoPacket> map = channelPacketMap.get(address);
        if (map != null) {
            result = map.put(id,future);
        } else {
            ConcurrentHashMap<Long, IoPacket> concurrentHashMap = new ConcurrentHashMap<>(8192, 0.75F, 512);
            result = concurrentHashMap.put(id,future);
            channelPacketMap.put(address,concurrentHashMap);
        }
        return result;
    }

    public IoPacket remove(SocketAddress address, Object id) {
        ConcurrentHashMap<Long, IoPacket> map = channelPacketMap.get(address);
        return map.remove(id);
    }
}
