package com.tencent.teresa.server;

import com.tencent.teresa.codec.IoPacket;
import com.tencent.teresa.processor.Processor;
import com.tencent.teresa.serializer.Serializer;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ServerProcessor implements Processor<IoPacket,IoPacket> {
    private Map<Object, MethodHanler> methodHanlerMap;
    private static final Logger logger = LoggerFactory.getLogger(ServerProcessor.class);
    private Serializer serializer;

    public ServerProcessor(Map<Object, MethodHanler> methodHanlerMap,Serializer serializer) {
        this.methodHanlerMap = methodHanlerMap;
        this.serializer = serializer;
    }

    @Override
    public IoPacket process(IoPacket packet, Channel ioChannel) throws Exception {
        MethodHanler methodHanler = methodHanlerMap.get(String.valueOf(packet.getSubcmd()));
        //TODO: subcmd not find
        if (methodHanler == null) {
            logger.error("not find methodHandler for subcmd {}",packet.getSubcmd());
            throw new IllegalArgumentException("not find methodHandler for subcmd:" + packet.getSubcmd());
        }
        try {
            Object obj = methodHanler.invoke(packet,serializer);
            if (obj != null) {
                //todo 1.性能优化 2.byte对Json不友好
                IoPacket rsp = packet.newResponsePacket(packet,0,"",obj,serializer);
                return rsp;
            }
        } catch (Exception e) {
            logger.error("error acquire methodHandler invoke" + packet.getCmd(), e);
        }
        return null;
    }
}
