package com.spirit.teresa.client.annotation;

import com.spirit.teresa.client.spring.config.ClientConfig;
import com.spirit.teresa.config.IliveCodecConfig;
import com.spirit.teresa.config.ProtocolEnum;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("client-spring.xml")
//@ClientConfig(protocol = "tcp")
@ClientConfig(protocol = ProtocolEnum.UDP)
@IliveCodecConfig(isClient = true,serializer = "pb")
public class TeresaClientConfig {
}
