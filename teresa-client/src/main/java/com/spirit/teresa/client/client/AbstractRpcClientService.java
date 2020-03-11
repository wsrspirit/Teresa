package com.spirit.teresa.client.client;

import com.spirit.teresa.client.ClientRpcHandler;
import com.spirit.teresa.client.future.IoPacketFuture;
import com.spirit.teresa.client.future.SendPacketFutureFactory;
import com.spirit.teresa.client.pool.RpcChannelManager;
import com.spirit.teresa.route.RouterInfo;
import com.spirit.teresa.route.RouterService;
import com.spirit.teresa.codec.IoPacket;
import com.spirit.teresa.codec.IoPacketCodec;
import com.spirit.teresa.timeout.DefaultTimeoutManager;
import com.spirit.teresa.timeout.TimeoutManager;
import com.spirit.teresa.worker.WorkerService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.pool.FixedChannelPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractRpcClientService<T_REQ extends IoPacket,T_RSP extends IoPacket> implements RpcClientService<T_REQ,T_RSP>,InitializingBean {

    protected int connectTimeout = 10000;
    private static final Logger logger = LoggerFactory.getLogger(AbstractRpcClientService.class);
    protected TimeoutManager timeoutManager;
    @Autowired
    protected RouterService routerService;
    @Autowired
    protected IoPacketCodec ioPacketCodec;
    @Autowired
    protected WorkerService workerService;

    protected ClientRpcHandler rpcHandler;
    protected Bootstrap bootstrap;
    protected RpcChannelManager channelManager;
    protected SendPacketFutureFactory sendPacketFutureFactory;

    public AbstractRpcClientService() {
        timeoutManager = new DefaultTimeoutManager();
    }

    @Override
    public T_RSP sync(T_REQ req, long timeout) throws Exception {
        IoPacketFuture<T_RSP> future = async(req, timeout);
        return future.get();
    }

    @Override
    public IoPacketFuture<T_RSP> async(final T_REQ req, long timeout) throws Exception {
        RouterInfo routerInfo = routerService.route(req.getRouterId());
        req.setRouterAddr(routerInfo.getSocketAddress());

        FixedChannelPool channelPool = channelManager.get(req.getRouterAddr());
        io.netty.util.concurrent.Future<Channel> future = channelPool.acquire();

        IoPacketFuture ioPacketFuture = sendPacketFutureFactory.newSendPacketFuture(req,timeout,routerInfo,future);
        if (channelManager.storeSendPackage(routerInfo.getSocketAddress(),(Long)req.getSeq() ,ioPacketFuture) != null) {
            logger.error("FATAL ERROR!! duplicate ioseq acquire lookups! seq=" + req.getSeq() + ", subCmd=" + req.getCmd());
        }

        future.addListener(futureResult -> {
            if (futureResult.isSuccess()) {
                Channel channel = future.getNow();
                channel.writeAndFlush(req);
                channelPool.release(channel);
            } else {
                logger.error("channel manager get channel future err, maybe connect remote fail");
//                channelManager.remove(routerInfo.getSocketAddress(),req.getSeq());
            }
        });

        return ioPacketFuture;
    }

    public RouterService getRouterService() {
        return routerService;
    }

    public void setRouterService(RouterService routerService) {
        this.routerService = routerService;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public TimeoutManager getTimeoutManager() {
        return timeoutManager;
    }

    public void setTimeoutManager(TimeoutManager timeoutManager) {
        this.timeoutManager = timeoutManager;
    }

    public IoPacketCodec getIoPacketCodec() {
        return ioPacketCodec;
    }

    public void setIoPacketCodec(IoPacketCodec ioPacketCodec) {
        this.ioPacketCodec = ioPacketCodec;
    }

    public WorkerService getWorkerService() {
        return workerService;
    }

    public void setWorkerService(WorkerService workerService) {
        this.workerService = workerService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.rpcHandler = new ClientRpcHandler(workerService);
        start();
    }
}
