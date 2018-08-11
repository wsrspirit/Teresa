package com.tencent.teresa.registry;

import java.net.InetSocketAddress;

public interface RegistryService {
    boolean registry(String serverAddress,Object cmd,Object subcmd);
}
