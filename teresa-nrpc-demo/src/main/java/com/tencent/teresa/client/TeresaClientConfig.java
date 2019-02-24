package com.tencent.teresa.client;

import com.tencent.teresa.client.spring.config.ClientConfig;
import com.tencent.teresa.config.IliveCodecConfig;
import com.tencent.teresa.config.NrpcCodecConfig;
import com.tencent.teresa.utils.U;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource(value = "classpath:client-spring.xml")
//@ClientConfig(protocol = "tcp")
@ClientConfig(protocol = "udp")
@NrpcCodecConfig(serializer = U.PB_SERIALIZER)
public class TeresaClientConfig {
}
