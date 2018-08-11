package com.tencent.teresa.client;

import com.tencent.teresa.client.future.SendPacketFutureFactory;
import com.tencent.teresa.client.pool.RpcChannelManager;
import com.tencent.teresa.codec.IoPacket;
import com.tencent.teresa.codec.udp.UdpDecoderAdapter;
import com.tencent.teresa.codec.udp.UdpEncoderAdapter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UdpRpcClient<T_REQ extends IoPacket,T_RSP extends IoPacket> extends AbstractRpcClientService<T_REQ,T_RSP>{
    private static final Logger logger = LoggerFactory.getLogger(UdpRpcClient.class);

    @Override
    public void start() {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
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
                .group(new NioEventLoopGroup())
                .handler(channelChannelInitializer)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout);

        this.bootstrap = bootstrap;
        channelManager = new RpcChannelManager(bootstrap,channelChannelInitializer);
        rpcHandler.setChannelManager(channelManager);
        this.sendPacketFutureFactory = new SendPacketFutureFactory(timeoutManager,channelManager);
        logger.info("UdpRpcClient start");
    }
}
