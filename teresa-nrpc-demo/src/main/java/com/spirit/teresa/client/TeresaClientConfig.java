package com.spirit.teresa.client;

import com.spirit.teresa.client.spring.config.ClientConfig;
import com.spirit.teresa.config.NrpcCodecConfig;
import com.spirit.teresa.config.ProtocolEnum;
import com.spirit.teresa.config.SerializerEnum;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource(value = "classpath:client-spring.xml")
//@ClientConfig(protocol = "tcp")
@ClientConfig(protocol = ProtocolEnum.UDP)
@NrpcCodecConfig(serializer = SerializerEnum.PROTOBUF_SERIALIZER)
public class TeresaClientConfig {
}
