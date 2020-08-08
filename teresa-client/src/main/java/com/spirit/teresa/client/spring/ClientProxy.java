package com.spirit.teresa.client.spring;

import com.spirit.teresa.client.client.AbstractRpcClientService;
import com.spirit.teresa.client.client.RpcClientService;
import com.spirit.teresa.client.future.IoPacketFuture;
import com.spirit.teresa.codec.IoPacket;
import com.spirit.teresa.exception.TeresaException;
import com.spirit.teresa.route.RouterInfo;
import com.spirit.teresa.serializer.ProtobufSerializer;
import com.spirit.teresa.serializer.Serializer;
import com.spirit.teresa.utils.ErrorCode;
import io.reactivex.Flowable;
import io.reactivex.processors.ReplayProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class ClientProxy implements InvocationHandler {
    private static final Logger logger = LoggerFactory.getLogger(ClientProxy.class);

    protected String bigCmd;
    protected RpcClientService<IoPacket,IoPacket> rpcClientService;
    protected AtomicLong seq = new AtomicLong(0);
    protected Serializer serializer;
    protected Map<String,Map<String,Object>> methodAttrMap;

    @Autowired
    public ClientProxy(Serializer serializer) {
        this.serializer = serializer;
    }

    public ClientProxy(String bigCmd, Map<String,Map<String,Object>> methodAttrMap, RpcClientService rpcClientService) {
        this.bigCmd = bigCmd;
        this.methodAttrMap = methodAttrMap;
        this.rpcClientService = rpcClientService;
        serializer = new ProtobufSerializer();
    }

    public Object invoke0(Object proxy, Method method, Object[] args,Map<String,Object> attrMap) throws Throwable {
        IoPacket requestPacket = (IoPacket) args[1];
        Object bizRequest = args[0];
        requestPacket.setCmd(bigCmd);
        String subCmd = (String)attrMap.get("subCmd");
        int timeout = (int)attrMap.get("timeout");
        boolean async = (boolean) attrMap.get("async");
        requestPacket.setSubCmd(subCmd);
        requestPacket.setSeq(seq.getAndIncrement());
        byte[] bizContentBytes = serializer.serialize(bizRequest);
        requestPacket.setBizContentBytes(bizContentBytes);
        RouterInfo routerInfo = ((AbstractRpcClientService)rpcClientService).getRouterService().route(bigCmd);
        requestPacket.setRouterAddr(routerInfo.getSocketAddress());
        requestPacket.setRequest(true);

        try {
            if (async){
                return Flowable.defer(() -> {
                    IoPacketFuture ioPacketFuture = rpcClientService.async(requestPacket,timeout);
                    ReplayProcessor<Object> flowable = ReplayProcessor.create(1);
                    ioPacketFuture.setCallbackListener(result -> {
                        IoPacket packet = (IoPacket) result;
                        byte[] bytes = packet.getBizContentBytes();
                        try {
                            Object rsp = serializer.deserialize((Class)((ParameterizedType)method.getGenericReturnType()).getActualTypeArguments()[0],bytes);
                            if (rsp != null) {
                                flowable.onNext(rsp);
                            }
                            flowable.onComplete();
                        } catch (Exception e) {
                            flowable.onError(e);
                        }

                    });
                    return flowable;
                });
            } else {
                IoPacket rsp = rpcClientService.sync(requestPacket,timeout);
                byte[] bytes = rsp.getBizContentBytes();
                if (rsp.getRetCode() != ErrorCode.SUCCESS.getErrCode()) {
                    throw new TeresaException(rsp.getRetCode(),rsp.getErrMsg());
                }
                return serializer.deserialize(method.getReturnType(),bytes);
            }
        } catch (Exception e) {
            logger.error("do client rpc method err: ",e.getMessage());
            throw e;
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        logger.info("ObjectProxy execute methodName {} bigCmd {} ",method.getName(),bigCmd);
        if (!methodAttrMap.containsKey(method.getName())) {
            throw new IllegalArgumentException("find no declare subCmd method : " + method.getName());
        }

        Map<String,Object> attrMap = methodAttrMap.get(method.getName());
        return invoke0(proxy, method, args,attrMap);
    }

    public String getBigCmd() {
        return bigCmd;
    }

    public void setBigCmd(String bigCmd) {
        this.bigCmd = bigCmd;
    }

    public RpcClientService<IoPacket, IoPacket> getRpcClientService() {
        return rpcClientService;
    }

    public void setRpcClientService(RpcClientService<IoPacket, IoPacket> rpcClientService) {
        this.rpcClientService = rpcClientService;
    }

    public Map<String, Map<String, Object>> getMethodAttrMap() {
        return methodAttrMap;
    }

    public void setMethodAttrMap(Map<String, Map<String, Object>> methodAttrMap) {
        this.methodAttrMap = methodAttrMap;
    }

    public static <T> T newInstance(Class<T> innerInterface, String bigCmd, Map<String,Map<String,Object>> methodAttrMap,
                                    RpcClientService rpcClientService, ClientProxy proxy) {
        ClassLoader classLoader = innerInterface.getClassLoader();
        Class[] interfaces = new Class[] { innerInterface };
        proxy.setBigCmd(bigCmd);
        proxy.setRpcClientService(rpcClientService);
        proxy.setMethodAttrMap(methodAttrMap);
        return (T) Proxy.newProxyInstance(classLoader, interfaces, proxy);
    }
}