package com.spirit.teresa.codec;

import com.dyuproject.protostuff.LinkedBuffer;
import com.spirit.teresa.codec.base.BaseEncoder;
import com.spirit.teresa.packet.NrpcPacket;
import com.spirit.teresa.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NrpcPackageEncoder extends BaseEncoder {
    private boolean isClient;
    private static final Logger logger = LoggerFactory.getLogger(NrpcPackageEncoder.class);
    public NrpcPackageEncoder(boolean isClient,Serializer serializer) {
        super(serializer);
        this.isClient = isClient;
    }

    @Override
    protected void encode0(ChannelHandlerContext ctx, IoPacket msg, ByteBuf out) throws Exception {
        LinkedBuffer linkedBuffer = ctx.channel().attr(ATTR_KEY_LINKEDBUFFER).get();
        if (linkedBuffer == null) {
            logger.error("client mode {} find linkedBuffer null",isClient);
        }

        NrpcPacket packet = (NrpcPacket)msg;
        byte[] bytes = serializer.serialize(packet,linkedBuffer);
        int lengh = bytes.length + 6;

        out.writeByte(NrpcPacket.START_BYTE);
        out.writeInt(lengh);
        out.writeBytes(bytes);
        out.writeByte(NrpcPacket.END_BYTE);
    }
}
