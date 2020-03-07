package com.spirit.teresa.codec.base;

import com.dyuproject.protostuff.LinkedBuffer;
import com.spirit.teresa.codec.IoPacket;
import com.spirit.teresa.codec.IoPacketEncoder;
import com.spirit.teresa.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseEncoder extends IoPacketEncoder<IoPacket> {
    private static final Logger logger = LoggerFactory.getLogger(BaseEncoder.class);
    protected Serializer serializer;
    private static final String LINKED_BUFFER_KEY = "linked_buffer";
    public static final AttributeKey<LinkedBuffer> ATTR_KEY_LINKEDBUFFER = AttributeKey.valueOf(LINKED_BUFFER_KEY);
    public static final int BUFFER_SIZE = 512;

    public BaseEncoder(Serializer serializer) {
        this.serializer = serializer;
    }

    private void initLinkedBufferIfNull(Channel channel) {
        Attribute<LinkedBuffer> attr = channel.attr(ATTR_KEY_LINKEDBUFFER);
        if (attr.get() == null) {
            logger.error("init linked buffer");
            attr.set(LinkedBuffer.allocate(BUFFER_SIZE));
        }
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, IoPacket msg, ByteBuf out) throws Exception {
        initLinkedBufferIfNull(ctx.channel());
        encode0(ctx,msg,out);
    }

    abstract protected void encode0(ChannelHandlerContext ctx, IoPacket msg, ByteBuf out) throws Exception;
}
