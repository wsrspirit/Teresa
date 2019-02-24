package com.tencent.teresa.client;

import com.tencent.teresa.client.future.IoPacketFuture;
import com.tencent.teresa.client.future.SendPacketFutureFactory;
import com.tencent.teresa.client.pool.RpcChannelManager;
import com.tencent.teresa.route.RouterInfo;
import com.tencent.teresa.route.RouterService;
import com.tencent.teresa.codec.IoPacket;
import com.tencent.teresa.codec.IoPacketCodec;
import com.tencent.teresa.timeout.TimeoutManager;
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
    @Autowired
    protected TimeoutManager timeoutManager;
    @Autowired
    protected RouterService routerService;
    @Autowired
    protected IoPacketCodec ioPacketCodec;

    protected ClientRpcHandler rpcHandler;
    protected Bootstrap bootstrap;
    protected RpcChannelManager channelManager;
    protected SendPacketFutureFactory sendPacketFutureFactory;

    public AbstractRpcClientService() {
        this.rpcHandler = new ClientRpcHandler();
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
        future.addListener(futureResult -> {
            if (futureResult.isSuccess()) {
                Channel channel = future.getNow();
                channel.writeAndFlush(req);
                channelPool.release(channel);
            } else {
                logger.error("channel manager get channel future err, maybe connect remote fail");
            }
        });
        IoPacketFuture ioPacketFuture = sendPacketFutureFactory.newSendPacketFuture(req,timeout,routerInfo,future);
        if (channelManager.put(routerInfo.getSocketAddress(),(Long)req.getSeq() ,ioPacketFuture) != null) {
            logger.error("FATAL ERROR!! duplicate ioseq acquire lookups! seq=" + req.getSeq() + ", subCmd=" + req.getCmd());
        }

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

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }
}
