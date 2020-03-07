package com.spirit.teresa.registry;

/**
 * 微服务注册代码
 */
public interface RegistryService {
    boolean registry(String serverAddress,Object cmd,Object subcmd);
}
