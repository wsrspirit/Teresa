package com.spirit.teresa.client.future;

import com.spirit.teresa.client.pool.RpcChannelManager;
import com.spirit.teresa.route.RouterInfo;
import com.spirit.teresa.codec.IoPacket;
import com.spirit.teresa.exception.RpcTimeoutException;
import com.spirit.teresa.timeout.TimeoutManager;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;

public class SendPacketFutureFactory {
    private TimeoutManager timeoutManager;
    private RpcChannelManager channelManager;
    private static final Logger logger = LoggerFactory.getLogger(SendPacketFutureFactory.class);

    public SendPacketFutureFactory(TimeoutManager timeoutManager, RpcChannelManager channelManager) {
        this.timeoutManager = timeoutManager;
        this.channelManager = channelManager;
    }

    public SendPacketFuture newSendPacketFuture(IoPacket req, long timeout,RouterInfo routerInfo,
                                                io.netty.util.concurrent.Future<Channel> channelFuture) {
        SendPacketFuture sendPacketFuture = new SendPacketFuture(req);
        Future<?> timeoutTask = timeoutManager.watch(() -> {
            //channel can be null for connect failed!
            Channel channel = channelFuture.getNow();
            IoPacketFuture item = (IoPacketFuture)channelManager.remove(routerInfo.getSocketAddress(),req.getSeq());
            if (item == null) {
                logger.error("can not find subCmd {} seq {} from channel manager, maybe client is too hard to send a packet");
                return;
            } else if (!item.getIoPacket().equals(req)) {
                logger.error("FATAL ERROR!! unmatched ioseq & req, lookups held too many items without timeout checking?");
            }

            String errMsg = item.getIoPacket().getCmd() + " timeout, " + req.getRouterAddr().getHostName() + ":" + req.getRouterAddr().getPort() + ", seq="+item.getIoPacket().getSeq();
            sendPacketFuture.setResult(new RpcTimeoutException(errMsg));
        },timeout);
        sendPacketFuture.setTimeoutTask(timeoutTask);
        return sendPacketFuture;
    }
}
