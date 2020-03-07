package com.spirit.teresa.server;

import com.spirit.teresa.config.NrpcCodecConfig;
import com.spirit.teresa.config.ProtocolEnum;
import com.spirit.teresa.config.SerializerEnum;
import com.spirit.teresa.config.WorkerServiceModeEnum;
import com.spirit.teresa.config.woker.WorkerServiceConfig;
import com.spirit.teresa.registry.DefaultRegistryService;
import com.spirit.teresa.registry.RegistryService;
import com.spirit.teresa.server.config.ServerConfig;
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
