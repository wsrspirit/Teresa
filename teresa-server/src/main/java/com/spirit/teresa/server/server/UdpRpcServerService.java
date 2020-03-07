package com.spirit.teresa.server.server;

import com.spirit.teresa.codec.udp.UdpDecoderAdapter;
import com.spirit.teresa.codec.udp.UdpEncoderAdapter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UdpRpcServerService extends AbstractRpcServerService {
    private static final Logger logger = LoggerFactory.getLogger(UdpRpcServerService.class);

    @Override
    public void start() throws Exception {
        String[] array = serverAddress.split(":");
        String host = array[0];
        int port = Integer.parseInt(array[1]);

        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ChannelFuture f = bootstrap
                .channel(NioDatagramChannel.class)
                .group(new NioEventLoopGroup(1))
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator())
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(workerGroup,"e", new UdpEncoderAdapter(ioPacketCodec.getEncoder(ch)));
                        p.addLast(workerGroup,"d", new UdpDecoderAdapter(ioPacketCodec.getDecoder(ch)));
                        p.addLast(workerGroup,"h", serverRpcHandler);
                    };
                }).bind(host,port);

        try {
            f.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (!f.isSuccess()) {
            throw new IllegalStateException("bind failed, addr has been used? addr:" + serverAddress, f.cause());
        }
        logger.info("udp server start! ip:" + serverAddress);
    }

    @Override
    public void stop() {

    }
}
