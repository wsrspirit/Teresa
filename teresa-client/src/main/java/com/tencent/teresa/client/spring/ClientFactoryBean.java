package com.tencent.teresa.client.spring;

import com.tencent.teresa.client.RpcClientService;
import com.tencent.teresa.client.TcpRpcClient;
import com.tencent.teresa.client.spring.annotation.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

public class ClientFactoryBean implements InitializingBean,FactoryBean, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(ClientFactoryBean.class);
    private String interfaceName;
    private int bigCmd;
    private ApplicationContext applicationContext;
    Map<String,Map<String,Integer>> methodAttrMap;
    private String clientProxy;
    private String clientInstance;

    @Override
    public Object getObject() throws Exception {
        RpcClientService rpcClientService = (RpcClientService)applicationContext.getBean(clientInstance);
        ClientProxy proxy = (ClientProxy)applicationContext.getBean(clientProxy);
        Class innerClass = Class.forName(interfaceName);
        if (innerClass.isInterface()) {
            return ClientProxy.newInstance(innerClass,bigCmd,methodAttrMap, rpcClientService,proxy);
        }
        logger.error("not interface");
        //todo how to stop
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //启动
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public int getBigCmd() {
        return bigCmd;
    }

    public void setBigCmd(int bigCmd) {
        this.bigCmd = bigCmd;
    }

    public Map<String, Map<String, Integer>> getMethodAttrMap() {
        return methodAttrMap;
    }

    public void setMethodAttrMap(Map<String, Map<String, Integer>> methodAttrMap) {
        this.methodAttrMap = methodAttrMap;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public String getClientProxy() {
        return clientProxy;
    }

    public void setClientProxy(String clientProxy) {
        this.clientProxy = clientProxy;
    }

    public String getClientInstance() {
        return clientInstance;
    }

    public void setClientInstance(String clientInstance) {
        this.clientInstance = clientInstance;
    }
}
