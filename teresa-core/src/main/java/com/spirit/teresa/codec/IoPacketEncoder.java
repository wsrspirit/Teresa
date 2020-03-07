package com.spirit.teresa.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public abstract class IoPacketEncoder<T_MSG extends IoPacket> extends MessageToByteEncoder<T_MSG> {
    public void doEncode(ChannelHandlerContext ctx, T_MSG msg, ByteBuf buf) throws Exception{
        this.encode(ctx, msg, buf);
    }
}
