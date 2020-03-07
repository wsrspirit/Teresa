package com.spirit.teresa.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public abstract class IoPacketDecoder extends ByteToMessageDecoder {
    public void doDecode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception{
        this.decode(ctx, buffer, out);
    }
}
