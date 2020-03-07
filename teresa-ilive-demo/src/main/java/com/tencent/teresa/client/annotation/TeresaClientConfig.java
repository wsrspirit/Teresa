package com.tencent.teresa.client.annotation;

import com.tencent.teresa.client.spring.config.ClientConfig;
import com.tencent.teresa.config.IliveCodecConfig;
import com.tencent.teresa.config.ProtocolEnum;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("client-spring.xml")
//@ClientConfig(protocol = "tcp")
@ClientConfig(protocol = ProtocolEnum.UDP)
@IliveCodecConfig(isClient = true,serializer = "pb")
public class TeresaClientConfig {
}
