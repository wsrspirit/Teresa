package com.spirit.teresa.server.server;

import com.dyuproject.protostuff.Message;
import com.spirit.teresa.server.MethodHandler;
import com.spirit.teresa.server.ServerRpcHandler;
import com.spirit.teresa.worker.WorkerService;
import com.spirit.teresa.codec.AbstractIoPacket;
import com.spirit.teresa.codec.IoPacketCodec;
import com.spirit.teresa.registry.RegistryService;
import com.spirit.teresa.serializer.Serializer;
import com.spirit.teresa.server.annotation.ServerMethod;
import com.spirit.teresa.server.annotation.ServerService;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractRpcServerService implements RpcServerService,ApplicationContextAware,InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(AbstractRpcServerService.class);

    protected Map<Object, MethodHandler> methodHanlerMap = new HashMap<>();

    protected String serverAddress;

    @Autowired
    protected IoPacketCodec ioPacketCodec;
    @Autowired
    protected WorkerService workerService;
    @Autowired
    protected Serializer serializer;
    @Autowired
    protected RegistryService registryService;
    protected ServerRpcHandler serverRpcHandler;


    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        //HSF 方式的调用
        Map<String, Object> map = ctx.getBeansWithAnnotation(ServerService.class);
        if (MapUtils.isNotEmpty(map)) {
            for (Object serviceBean : map.values()) {
                ServerService rpcServiceAnnotation = serviceBean.getClass().getAnnotation(ServerService.class);
                int cmd = rpcServiceAnnotation.cmd();
                for (Method method : serviceBean.getClass().getDeclaredMethods()) {
                    if (method.getAnnotation(ServerMethod.class) != null) {
                        String subcmd = method.getAnnotation(ServerMethod.class).subcmd();
                        registryService.registry(serverAddress,cmd,subcmd);

                        Class<?>[] parameterTypes = method.getParameterTypes();
                        boolean success = checkParam(parameterTypes);
                        if (!success) {
                            stop();
                        }
                        Class<?> returnType = method.getReturnType();
                        logger.info("load annotation {} method {} cmd {} subCmd {} para {} return {}",serviceBean.getClass(),
                                method.getName(),cmd,subcmd,parameterTypes[0].getName(),returnType.getName());
                        MethodHandler methodHandler = new MethodHandler(serviceBean,method,subcmd,parameterTypes,returnType);
                        methodHanlerMap.put(subcmd, methodHandler);
                    }
                }
            }
        }

        initServerRpcHandler();
    }


    private void initServerRpcHandler() {
        serverRpcHandler = new ServerRpcHandler(workerService,methodHanlerMap,serializer);
    }

    private boolean checkParam(Class<?>[] parameterTypes) {
        if (parameterTypes.length == 2 &&
                Message.class.isAssignableFrom(parameterTypes[0]) &&
                AbstractIoPacket.class.isAssignableFrom(parameterTypes[1])) {
            return true;
        }

        logger.error("checkParam err parameterTypes {} ",parameterTypes);
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }

    public String getServerAddress() {
        return serverAddress;
    }

    @Override
    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public WorkerService getWorkerService() {
        return workerService;
    }

    @Override
    public void setWorkerService(WorkerService workerService) {
        this.workerService = workerService;
    }

    public Serializer getSerializer() {
        return serializer;
    }

    @Override
    public void setSerializer(Serializer serializer) {
        this.serializer = serializer;
    }

}
