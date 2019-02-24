package com.tencent.teresa.server.annotation;

import com.tencent.teresa.config.IliveCodecConfig;
import com.tencent.teresa.server.config.ServerConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("server-spring.xml")
//@ServerConfig(protocol = "udp",serverAddress = "10.139.233.230:18866")
//@ServerConfig(protocol = "udp",serverAddress = "10.100.73.164:18866")
//@ServerConfig(protocol = "tcp",serverAddress = "10.139.233.230:18866")
//@ServerConfig(protocol = "udp",serverAddress = "10.19.85.79:18866")
//@ServerConfig(protocol = "tcp",serverAddress = "127.0.0.1:18866")
@ServerConfig(protocol = "udp",serverAddress = "127.0.0.1:18866")
@IliveCodecConfig(isClient = false,serializer = "pb")
public class TeresaServerConfig {
}
