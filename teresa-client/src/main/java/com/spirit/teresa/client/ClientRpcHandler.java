package com.spirit.teresa.client;

import com.spirit.teresa.client.future.SendPacketFuture;
import com.spirit.teresa.client.pool.RpcChannelManager;
import com.spirit.teresa.codec.IoPacket;
import com.spirit.teresa.utils.U;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandler.Sharable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Sharable
public class ClientRpcHandler<T_REQ extends IoPacket, T_RSP extends IoPacket> extends SimpleChannelInboundHandler<IoPacket> {
    private static final Logger logger = LoggerFactory.getLogger(ClientRpcHandler.class);
    private RpcChannelManager channelManager;
    private ExecutorService executor;

    public ClientRpcHandler() {
        executor = Executors.newFixedThreadPool(U.DEFAULT_THREADS,new ThreadFactory() {
            final AtomicInteger TID = new AtomicInteger(0);
            final String GROUP_NAME = "WORK_THREAD_";
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, GROUP_NAME + TID.getAndIncrement());
            }
        });
    }

    public RpcChannelManager getChannelManager() {
        return channelManager;
    }

    public void setChannelManager(RpcChannelManager channelManager) {
        this.channelManager = channelManager;
    }

    /**
     * 这里面需要使用线程池承接回调，因为回调可能会被阻塞，但netty线程不可以被阻塞
     * @param channelHandlerContext
     * @param ioPacket
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, IoPacket ioPacket) throws Exception {
        Channel channel = channelHandlerContext.channel();
        SendPacketFuture<IoPacket> future = (SendPacketFuture<IoPacket>) channelManager.remove(channel.remoteAddress(),ioPacket.getSeq());
        if (future == null) {
            logger.error("subCmd {} seq {} routerId {} response can not find it's future could not be found, mostly because remote server rpc timeout"
                    ,ioPacket.getCmd(),ioPacket.getSeq(),ioPacket.getRouterId());
            return;
        }

        executor.execute(() -> {
            future.setResult(ioPacket);
            future.getTimeoutTask().cancel(false);
            future.callback();
        });
    }
}
