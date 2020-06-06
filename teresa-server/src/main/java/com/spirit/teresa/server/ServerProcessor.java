package com.spirit.teresa.server;

import com.spirit.teresa.codec.IoPacket;
import com.spirit.teresa.exception.TeresaException;
import com.spirit.teresa.processor.Processor;
import com.spirit.teresa.serializer.Serializer;
import com.spirit.teresa.utils.ErrorCode;
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

        IoPacket rsp;

        try {
            //todo beforAspect
            Object obj = methodHandler.invoke(packet,serializer);

            //todo 貌似不用做非空判断,protobuf不赞成返回空？语法是否正确呢
            //todo 1.性能优化 2.byte对Json不友好
            rsp = packet.newResponsePacket(packet,0,"",obj,serializer);

            //todo beforAspect
        } catch (Throwable e) {
            logger.error("Throwable error acquire methodHandler invoke cmd: {} err",packet.getCmd(), e);
            //传递异常信息
            rsp = packet.newResponsePacket(packet, ErrorCode.SERVER_INNER_ERR.errCode, e.getCause().getMessage(), null,serializer);
        }

        return rsp;
    }
}
