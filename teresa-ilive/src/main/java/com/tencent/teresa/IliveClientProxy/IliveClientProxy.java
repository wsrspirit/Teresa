package com.tencent.teresa.IliveClientProxy;

import com.dyuproject.protostuff.ByteString;
import com.tencent.teresa.client.AbstractRpcClientService;
import com.tencent.teresa.client.TcpRpcClient;
import com.tencent.teresa.client.spring.ClientProxy;
import com.tencent.teresa.packet.ILiveRequest;
import com.tencent.teresa.packet.ILiveResponse;
import com.tencent.teresa.route.RouterInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;

public class IliveClientProxy extends ClientProxy {
    private static final Logger logger = LoggerFactory.getLogger(IliveClientProxy.class);

    public IliveClientProxy() {
    }

    public IliveClientProxy(int bigCmd, Map<String, Map<String, Integer>> methodAttrMap, TcpRpcClient tcpRpcClient) {
        super(bigCmd, methodAttrMap, tcpRpcClient);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args, Map<String, Integer> attrMap) throws Throwable {
        ILiveRequest request = (ILiveRequest) args[1];
        request.setCmd(bigCmd);
        int subCmd = attrMap.get("cmd");
        int timeout = attrMap.get("timeout");
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
