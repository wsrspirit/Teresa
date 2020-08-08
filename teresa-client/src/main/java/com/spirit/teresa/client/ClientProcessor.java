package com.spirit.teresa.client;

import com.spirit.teresa.client.future.SendPacketFuture;
import com.spirit.teresa.client.pool.RpcChannelManager;
import com.spirit.teresa.codec.IoPacket;
import com.spirit.teresa.processor.Processor;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientProcessor implements Processor<IoPacket,IoPacket> {
    private static final Logger logger = LoggerFactory.getLogger(ClientProcessor.class);
    private RpcChannelManager channelManager;

    public ClientProcessor(RpcChannelManager channelManager) {
        this.channelManager = channelManager;
    }

    @Override
    public IoPacket process(IoPacket ioPacket, Channel ioChannel) {
        SendPacketFuture<IoPacket> future = (SendPacketFuture<IoPacket>) channelManager.remove(ioChannel.remoteAddress(),ioPacket.getSeq());
        if (future == null) {
            logger.error("subCmd {} seq {} routerId {} response can not find it's future could not be found, mostly because remote server rpc timeout"
                    ,ioPacket.getCmd(),ioPacket.getSeq(),ioPacket.getRouterId());
            return null;
        }
        future.setResult(ioPacket);
        future.getTimeoutTask().cancel(false);
        future.fireCallbackListener();
        return future.getIoPacket();
    }
}
