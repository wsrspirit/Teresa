package com.spirit.teresa.client;

import com.spirit.teresa.client.client.AbstractRpcClientService;
import com.spirit.teresa.client.future.SendPacketFutureFactory;
import com.spirit.teresa.client.pool.RpcChannelManager;
import com.spirit.teresa.codec.IoPacket;
import com.spirit.teresa.codec.udp.UdpDecoderAdapter;
import com.spirit.teresa.codec.udp.UdpEncoderAdapter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UdpRpcClient<T_REQ extends IoPacket,T_RSP extends IoPacket> extends AbstractRpcClientService<T_REQ,T_RSP> {
    private static final Logger logger = LoggerFactory.getLogger(UdpRpcClient.class);
    private NioEventLoopGroup workerGroup;
    private NioEventLoopGroup bossEventLoop;

    @Override
    public void start() {
        Bootstrap bootstrap = new Bootstrap();
        workerGroup = new NioEventLoopGroup();
        bossEventLoop = new NioEventLoopGroup();
        ChannelInitializer<Channel> channelChannelInitializer = new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();
                p.addLast(workerGroup, "e", new UdpEncoderAdapter(ioPacketCodec.getEncoder(ch)));
                p.addLast(workerGroup, "d", new UdpDecoderAdapter(ioPacketCodec.getDecoder(ch)));
                p.addLast(workerGroup, "h", rpcHandler);
            }
        };


        bootstrap.channel(NioDatagramChannel.class)
                .group(bossEventLoop)
                .handler(channelChannelInitializer)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout);

        this.bootstrap = bootstrap;
        channelManager = new RpcChannelManager(bootstrap,channelChannelInitializer);
        rpcHandler.setChannelManager(channelManager);
        this.sendPacketFutureFactory = new SendPacketFutureFactory(timeoutManager,channelManager);
        logger.error("UdpRpcClient start");
    }

    @Override
    public void shutdown() {
        logger.error("UdpRpcClient shutdown");
        workerGroup.shutdownGracefully();
        bossEventLoop.shutdownGracefully();
    }
}
