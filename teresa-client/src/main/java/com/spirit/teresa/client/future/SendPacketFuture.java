package com.spirit.teresa.client.future;

import com.spirit.teresa.codec.IoPacket;
import com.spirit.teresa.exception.RpcException;
import com.spirit.teresa.serializer.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

/**
 * @param <V> 这里的V就是IoPacket
 */
public class SendPacketFuture<V> implements IoPacketFuture<V> {
    private IoPacket ioPacket;
    private Future<?> timeoutTask;
    private V result;
    private static final Logger logger = LoggerFactory.getLogger(SendPacketFuture.class);
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    private CallbackListener<V> callbackListener;

    public SendPacketFuture(IoPacket ioPacket) {
        this.ioPacket = ioPacket;
    }

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

    @Override
    public void setCallbackListener(CallbackListener<V> listener) {
        this.callbackListener = listener;
    }

    @Override
    public void fireCallbackListener() {
        if (callbackListener != null) {
            callbackListener.callback(result);
        }
    }

}
