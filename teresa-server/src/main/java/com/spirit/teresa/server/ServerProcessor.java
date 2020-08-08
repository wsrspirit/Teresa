package com.spirit.teresa.server;

import com.spirit.teresa.codec.IoPacket;
import com.spirit.teresa.processor.Processor;
import com.spirit.teresa.serializer.Serializer;
import com.spirit.teresa.utils.ErrorCode;
import io.netty.channel.Channel;
import io.reactivex.Flowable;
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
        MethodHandler methodHandler = methodHanlerMap.get(String.valueOf(packet.getSubCmd()));
        //TODO: subCmd not find
        if (methodHandler == null) {
            logger.error("not find methodHandler for subCmd {}",packet.getSubCmd());
            throw new IllegalArgumentException("not find methodHandler for subCmd:" + packet.getSubCmd());
        }

        IoPacket respPacket = null;

        try {
            //todo beforAspect
            Object invokeRsp = methodHandler.invoke(packet,serializer);

            if (invokeRsp instanceof Flowable) {
                ((Flowable) invokeRsp).subscribe(o -> {
                    Object asyncRsp = packet.newResponsePacket(packet,0,"",o,serializer);
                    ioChannel.writeAndFlush(asyncRsp);
                }, throwable -> {
                    logger.error("Throwable error acquire methodHandler invoke cmd: {} err",packet.getCmd(), throwable);
                    Object asyncRsp = packet.newResponsePacket(packet, ErrorCode.SERVER_INNER_ERR.errCode, ((Throwable)throwable).getCause().getMessage(), null,serializer);
                    ioChannel.writeAndFlush(asyncRsp);
                    }, () -> logger.debug("async write rsp complete"));
            } else {
                //todo 貌似不用做非空判断,protobuf不赞成返回空？语法是否正确呢
                //todo 1.性能优化 2.byte对Json不友好
                respPacket = packet.newResponsePacket(packet,0,"",invokeRsp,serializer);
            }

            //todo beforAspect
        } catch (Throwable e) {
            logger.error("Throwable error acquire methodHandler invoke cmd: {} err",packet.getCmd(), e);
            //传递异常信息
            respPacket = packet.newResponsePacket(packet, ErrorCode.SERVER_INNER_ERR.errCode, e.getCause().getMessage(), null,serializer);
        } finally {
            if (respPacket != null) {
                ioChannel.writeAndFlush(respPacket);
            }
        }

        return respPacket;
    }
}
