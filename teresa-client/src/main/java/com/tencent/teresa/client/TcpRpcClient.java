package com.tencent.teresa.client;

import com.tencent.teresa.client.future.SendPacketFutureFactory;
import com.tencent.teresa.client.pool.RpcChannelManager;
import com.tencent.teresa.codec.IoPacket;
import com.tencent.teresa.route.RouterInfo;
import com.tencent.teresa.route.RouterService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpRpcClient<T_REQ extends IoPacket,T_RSP extends IoPacket> extends AbstractRpcClientService<T_REQ,T_RSP> {

    private static final Logger logger = LoggerFactory.getLogger(TcpRpcClient.class);

    @Override
    public void start() {
        Bootstrap bootstrap = new Bootstrap();

        ChannelInitializer<Channel> initializer = new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();
                p.addLast("e", ioPacketCodec.getEncoder(ch));
                p.addLast("d", ioPacketCodec.getDecoder(ch));
                p.addLast("h", rpcHandler);
            }
        };

        bootstrap.channel(NioSocketChannel.class)
                .group(new NioEventLoopGroup())
                .handler(initializer)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout);

        this.bootstrap = bootstrap;
        channelManager = new RpcChannelManager(bootstrap,initializer);
        rpcHandler.setChannelManager(channelManager);
        this.sendPacketFutureFactory = new SendPacketFutureFactory(timeoutManager,channelManager);
        logger.info("TcpRpcClient start");
    }
}
