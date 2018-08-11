package com.tencent.teresa.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultRegistryService implements RegistryService {
    private static final Logger logger = LoggerFactory.getLogger(DefaultRegistryService.class);
    @Override
    public boolean registry(String serverAddress, Object cmd, Object subcmd) {
        logger.info("register addr {} cmd {} subcmd {}",serverAddress,cmd,subcmd);
        return false;
    }
}
