package com.tencent.teresa.registry;

public interface RegistryService {
    boolean registry(String serverAddress,Object cmd,Object subcmd);
}
