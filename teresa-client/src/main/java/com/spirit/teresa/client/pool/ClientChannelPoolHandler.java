package com.spirit.teresa.client.pool;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.pool.ChannelPoolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientChannelPoolHandler implements ChannelPoolHandler {
    private ChannelInitializer<? extends Channel> channelInitializer;
    private static final Logger logger = LoggerFactory.getLogger(ClientChannelPoolHandler.class);

    public ClientChannelPoolHandler(ChannelInitializer<? extends Channel> channelInitializer) {
        this.channelInitializer = channelInitializer;
    }

    @Override
    public void channelReleased(Channel ch) throws Exception {
        logger.debug("client channel pool release id {}",ch.id());
    }

    @Override
    public void channelAcquired(Channel ch) throws Exception {
        logger.debug("client channel pool acquired id {}",ch.id());
    }

    @Override
    public void channelCreated(Channel ch) throws Exception {
        logger.debug("client channel pool created id {}",ch.id());
        ch.pipeline().addLast(channelInitializer);
    }
}
