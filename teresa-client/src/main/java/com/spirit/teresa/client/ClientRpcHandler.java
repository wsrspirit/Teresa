package com.spirit.teresa.client;

import com.spirit.teresa.client.future.SendPacketFuture;
import com.spirit.teresa.client.pool.RpcChannelManager;
import com.spirit.teresa.codec.IoPacket;
import com.spirit.teresa.processor.Processor;
import com.spirit.teresa.utils.U;
import com.spirit.teresa.worker.WorkerService;
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
    private WorkerService workerService;
    private Processor clientProcessor;

    public ClientRpcHandler(WorkerService workerService) {
        this.workerService = workerService;
    }


    public void setChannelManager(RpcChannelManager channelManager) {
        clientProcessor = new ClientProcessor(channelManager);
    }

    /**
     * 这里面需要使用线程池承接回调，因为回调可能会被阻塞，但netty线程不可以被阻塞
     * 直接交给workerService处理
     * @param channelHandlerContext
     * @param ioPacket
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, IoPacket ioPacket) throws Exception {
        workerService.dispatch(channelHandlerContext.channel(),ioPacket,clientProcessor);
    }
}
