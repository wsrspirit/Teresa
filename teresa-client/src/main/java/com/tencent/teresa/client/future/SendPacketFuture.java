package com.tencent.teresa.client.future;

import com.tencent.teresa.codec.IoPacket;
import com.tencent.teresa.exception.RpcException;
import com.tencent.teresa.serializer.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

public class SendPacketFuture<V> implements IoPacketFuture<V> {
    private IoPacket ioPacket;
    private Future<?> timeoutTask;
    private Object result;
    private static final Logger logger = LoggerFactory.getLogger(SendPacketFuture.class);
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public SendPacketFuture(IoPacket ioPacket) {
        this.ioPacket = ioPacket;
    }

    @Override
    public Object getSeq() {
        return ioPacket.getSeq();
    }

    @Override
    public Object getCmd() {
        return ioPacket.getCmd();
    }

    @Override
    public Object getSubcmd() {
        return ioPacket.getSubcmd();
    }

    @Override
    public long getCreateTime() {
        return ioPacket.getCreateTime();
    }

    @Override
    public Object getRouterId() {
        return ioPacket.getRouterId();
    }

    @Override
    public InetSocketAddress getRouterAddr() {
        return ioPacket.getRouterAddr();
    }

    @Override
    public void setRouterAddr(InetSocketAddress addr) {
        ioPacket.setRouterAddr(addr);
    }

    @Override
    public int getEstimateSize() {
        return ioPacket.getEstimateSize();
    }

    @Override
    public long getRetCode() {
        return ioPacket.getRetCode();
    }

    @Override
    public String getErrorMsg() {
        return ioPacket.getErrorMsg();
    }

    @Override
    public IoPacket newResponsePacket(IoPacket reqPacket, int ec, String message, Object body, Serializer serializer) throws Exception {
        return ioPacket.newResponsePacket(reqPacket, ec, message, body,serializer);
    }

    @Override
    public Object getContent(Class clazz, Serializer serializer) throws Exception {
        return ioPacket.getContent(clazz, serializer);
    }

    @Override
    public void setContent(Object content, Serializer serializer) throws Exception {
        ioPacket.setContent(content,serializer);
    }


    @Override
    public IoPacket getIoPacket() {
        return ioPacket;
    }

    @Override
    public void setResult(V result) {
        this.result = result;
        countDownLatch.countDown();
    }

    public V get0() throws Exception {
        if (result instanceof IoPacket) {
            return (V) result;
        } else if (result instanceof Exception){
            throw (Exception) result;
        }
        logger.error("unexpected result {}",result);
        throw new RpcException("get unexpected result type");
    }

    @Override
    public V get() throws Exception {
        if (isDone()) {
            return get0();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return get0();
    }

    public Future<?> getTimeoutTask() {
        return timeoutTask;
    }

    public void setTimeoutTask(Future<?> timeoutTask) {
        this.timeoutTask = timeoutTask;
    }

    @Override
    public boolean isDone() {
        if (result != null) {
            return true;
        }
        return false;
    }
}
