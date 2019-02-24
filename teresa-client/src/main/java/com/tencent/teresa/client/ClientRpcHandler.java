package com.tencent.teresa.client;

import com.tencent.teresa.client.future.SendPacketFuture;
import com.tencent.teresa.client.pool.RpcChannelManager;
import com.tencent.teresa.codec.IoPacket;
import com.tencent.teresa.worker.WorkerService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandler.Sharable;

@Sharable
public class ClientRpcHandler<T_REQ extends IoPacket, T_RSP extends IoPacket> extends SimpleChannelInboundHandler<IoPacket> {
    private static final Logger logger = LoggerFactory.getLogger(ClientRpcHandler.class);
    private RpcChannelManager channelManager;

    public RpcChannelManager getChannelManager() {
        return channelManager;
    }

    public void setChannelManager(RpcChannelManager channelManager) {
        this.channelManager = channelManager;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, IoPacket ioPacket) throws Exception {
        Channel channel = channelHandlerContext.channel();
        SendPacketFuture<IoPacket> future = (SendPacketFuture<IoPacket>) channelManager.remove(channel.remoteAddress(),ioPacket.getSeq());
        if (future == null) {
            logger.error("subCmd {} seq {} routerId {} response can not find it's future could not be found, mostly because remote server rpc timeout"
                    ,ioPacket.getCmd(),ioPacket.getSeq(),ioPacket.getRouterId());
            return;
        }
        future.setResult(ioPacket);
        future.getTimeoutTask().cancel(false);
    }
}
