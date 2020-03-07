package com.tencent.teresa.client;

import com.tencent.teresa.client.spring.config.ClientConfig;
import com.tencent.teresa.config.NrpcCodecConfig;
import com.tencent.teresa.config.ProtocolEnum;
import com.tencent.teresa.config.SerializerEnum;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource(value = "classpath:client-spring.xml")
//@ClientConfig(protocol = "tcp")
@ClientConfig(protocol = ProtocolEnum.UDP)
@NrpcCodecConfig(serializer = SerializerEnum.PROTOBUF_SERIALIZER)
public class TeresaClientConfig {
}
