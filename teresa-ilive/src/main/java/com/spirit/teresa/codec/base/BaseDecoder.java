package com.spirit.teresa.codec.base;

import com.spirit.teresa.codec.IoPacketDecoder;
import com.spirit.teresa.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class BaseDecoder extends IoPacketDecoder {
    public static final Logger logger = LoggerFactory.getLogger(BaseDecoder.class);
    protected Serializer serializer;

    public BaseDecoder(Serializer serializer) {
        this.serializer = serializer;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        decode0(ctx, in, out);
    }

    protected abstract void decode0(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception ;
}
