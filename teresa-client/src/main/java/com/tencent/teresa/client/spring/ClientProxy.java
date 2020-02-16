package com.tencent.teresa.client.spring;

import com.tencent.teresa.client.client.RpcClientService;
import com.tencent.teresa.codec.IoPacket;
import com.tencent.teresa.serializer.ProtobufSerializer;
import com.tencent.teresa.serializer.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public abstract class ClientProxy implements InvocationHandler {
    private static final Logger logger = LoggerFactory.getLogger(ClientProxy.class);

    protected String bigCmd;
    protected RpcClientService<IoPacket,IoPacket> rpcClientService;
    protected AtomicLong seq = new AtomicLong(0);
    protected Serializer serializer;
    protected Map<String,Map<String,Object>> methodAttrMap;

    public ClientProxy(Serializer serializer) {
        this.serializer = serializer;
    }

    public ClientProxy(String bigCmd, Map<String,Map<String,Object>> methodAttrMap, RpcClientService rpcClientService) {
        this.bigCmd = bigCmd;
        this.methodAttrMap = methodAttrMap;
        this.rpcClientService = rpcClientService;
        serializer = new ProtobufSerializer();
    }

    public abstract Object invoke(Object proxy, Method method, Object[] args,Map<String,Object> attrMap) throws Throwable;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        logger.info("ObjectProxy execute methodName {} bigCmd {} ",method.getName(),bigCmd);
        if (!methodAttrMap.containsKey(method.getName())) {
            throw new IllegalArgumentException("find no declare subcmd method : " + method.getName());
        }

        Map<String,Object> attrMap = methodAttrMap.get(method.getName());
        return invoke(proxy, method, args,attrMap);
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