package com.spirit.teresa.server;

import com.spirit.teresa.codec.IoPacket;
import com.spirit.teresa.processor.Processor;
import com.spirit.teresa.serializer.Serializer;
import com.spirit.teresa.utils.CommonErr;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ServerProcessor implements Processor<IoPacket,IoPacket> {
    private Map<Object, MethodHandler> methodHanlerMap;
    private static final Logger logger = LoggerFactory.getLogger(ServerProcessor.class);
    private Serializer serializer;

    public ServerProcessor(Map<Object, MethodHandler> methodHanlerMap, Serializer serializer) {
        this.methodHanlerMap = methodHanlerMap;
        this.serializer = serializer;
    }

    @Override
    public IoPacket process(IoPacket packet, Channel ioChannel) {
        MethodHandler methodHandler = methodHanlerMap.get(String.valueOf(packet.getSubcmd()));
        //TODO: subcmd not find
        if (methodHandler == null) {
            logger.error("not find methodHandler for subcmd {}",packet.getSubcmd());
            throw new IllegalArgumentException("not find methodHandler for subcmd:" + packet.getSubcmd());
        }

        try {
            IoPacket rsp = packet.newResponsePacket(packet, CommonErr.SERVER_INNER_ERR.errCode, CommonErr.SERVER_INNER_ERR.errMsg, null,null);
            //todo beforAspect
            Object obj = methodHandler.invoke(packet,serializer);
            if (obj != null) {
                //todo 1.性能优化 2.byte对Json不友好
                rsp = packet.newResponsePacket(packet,0,"",obj,serializer);
            }
            //todo beforAspect
            return rsp;
        } catch (Exception e) {
            logger.error("error acquire methodHandler invoke" + packet.getCmd(), e);
        }

        return null;
    }
}
