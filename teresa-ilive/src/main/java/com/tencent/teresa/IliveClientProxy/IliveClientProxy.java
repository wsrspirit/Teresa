package com.tencent.teresa.IliveClientProxy;

import com.tencent.teresa.client.client.AbstractRpcClientService;
import com.tencent.teresa.client.TcpRpcClient;
import com.tencent.teresa.client.spring.ClientProxy;
import com.tencent.teresa.packet.ILiveRequest;
import com.tencent.teresa.packet.ILiveResponse;
import com.tencent.teresa.route.RouterInfo;
import com.tencent.teresa.serializer.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.Map;

public class IliveClientProxy extends ClientProxy {
    private static final Logger logger = LoggerFactory.getLogger(IliveClientProxy.class);

    @Autowired
    public IliveClientProxy(Serializer serializer) {
        super(serializer);
    }

    public IliveClientProxy(String bigCmd, Map<String, Map<String, Object>> methodAttrMap, TcpRpcClient tcpRpcClient) {
        super(bigCmd, methodAttrMap, tcpRpcClient);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args, Map<String, Object> attrMap) throws Throwable {
        ILiveRequest request = (ILiveRequest) args[1];
        request.setCmd(bigCmd);
        String subCmd = (String)attrMap.get("subCmd");
        int timeout = (int)attrMap.get("timeout");
        request.setSubcmd(subCmd);
        request.setSeq(seq.getAndIncrement());
        request.setContent(args[0],serializer);
        RouterInfo routerInfo = ((AbstractRpcClientService)rpcClientService).getRouterService().route(bigCmd);
        request.setRouterAddr(routerInfo.getSocketAddress());

        try {
            ILiveResponse rsp = (ILiveResponse)rpcClientService.sync(request,timeout);
            byte[] bytes = rsp.getEx().toByteArray();
            return serializer.deserialize(method.getReturnType(),bytes);
        } catch (Exception e) {
            logger.error("do client rpc method err: ",e);
            throw e;
        }
    }
}
