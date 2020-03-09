package com.spirit.teresa.server;

import com.spirit.teresa.processor.Processor;
import com.spirit.teresa.worker.WorkerService;
import com.spirit.teresa.codec.IoPacket;
import com.spirit.teresa.serializer.Serializer;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
@ChannelHandler.Sharable
public class ServerRpcHandler extends SimpleChannelInboundHandler<IoPacket> {
    private static final Logger logger = LoggerFactory.getLogger(ServerRpcHandler.class);
    private WorkerService workerService;
    private Processor<IoPacket,IoPacket> processor;

    public ServerRpcHandler(WorkerService workerService, Map<Object, MethodHandler> methodHanlerMap, Serializer serializer) {
        this.workerService = workerService;
        this.processor = new ServerProcessor(methodHanlerMap,serializer);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, IoPacket ioPacket) throws Exception {
        workerService.dispatch(channelHandlerContext.channel(),ioPacket,processor);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        logger.error("error acquire channel " + ctx.channel().remoteAddress(), cause);
    }

}
