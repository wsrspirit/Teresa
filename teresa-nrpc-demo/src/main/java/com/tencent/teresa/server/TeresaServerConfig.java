package com.tencent.teresa.server;

import com.tencent.teresa.config.NrpcCodecConfig;
import com.tencent.teresa.config.ProtocolEnum;
import com.tencent.teresa.config.WorkerServiceModeEnum;
import com.tencent.teresa.config.woker.WorkerServiceConfig;
import com.tencent.teresa.registry.DefaultRegistryService;
import com.tencent.teresa.registry.RegistryService;
import com.tencent.teresa.server.config.ServerConfig;
import com.tencent.teresa.config.SerializerEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource(value = "classpath:server-spring.xml")
//@ServerConfig(protocol = "udp",serverAddress = "10.139.233.230:18866")
//@ServerConfig(protocol = "udp",serverAddress = "10.100.73.164:18866")
//@ServerConfig(protocol = "tcp",serverAddress = "10.139.233.230:18866")
//@ServerConfig(protocol = "udp",serverAddress = "10.19.85.79:18866")
//@ServerConfig(protocol = "tcp",serverAddress = "127.0.0.1:18866")
@ServerConfig(protocol = ProtocolEnum.UDP,serverAddress = "127.0.0.1:18866")
@NrpcCodecConfig(serializer = SerializerEnum.PROTOBUF_SERIALIZER)
@WorkerServiceConfig(workerMode = WorkerServiceModeEnum.RX_THREAD_MODE)
public class TeresaServerConfig {
    @Bean
    public RegistryService registryService() {
        return new DefaultRegistryService();
    }
}
