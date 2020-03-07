package com.spirit.teresa.codec;

import com.dyuproject.protostuff.LinkedBuffer;
import com.spirit.teresa.codec.base.BaseEncoder;
import com.spirit.teresa.packet.IliveIdentifier;
import com.spirit.teresa.packet.ILiveRequest;
import com.spirit.teresa.packet.ILiveResponse;
import com.spirit.teresa.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Sharable
public class ILivePackageEncoder extends BaseEncoder {
    private boolean isClient;
    private static final Logger logger = LoggerFactory.getLogger(ILivePackageEncoder.class);
    public ILivePackageEncoder(boolean isClient,Serializer serializer) {
        super(serializer);
        this.isClient = isClient;
    }

    @Override
    protected void encode0(ChannelHandlerContext ctx, IoPacket msg, ByteBuf out) throws Exception {
        LinkedBuffer linkedBuffer = ctx.channel().attr(ATTR_KEY_LINKEDBUFFER).get();
        if (linkedBuffer == null) {
            logger.error("client mode {} find linkedBuffer null",isClient);
        }

        if (isClient) {
            ILiveRequest request = (ILiveRequest)msg;

            byte[] requestBytes = serializer.serialize(request,linkedBuffer);

            //总长度(两字节)+0x28+iLiveRequest/iLiveResponse+0x3
            int pkgLength = requestBytes.length + 4;
            out.writeShort((short) pkgLength);
            out.writeByte(IliveIdentifier.START_BYTE);
            out.writeBytes(requestBytes);
            out.writeByte(IliveIdentifier.END_BYTE);
        } else {
            ILiveResponse response = (ILiveResponse)msg;

            byte[] responseBytes = serializer.serialize(response,linkedBuffer);

            //总长度(两字节)+0x28+iLiveRequest/iLiveResponse+0x3
            int pkgLength = responseBytes.length + 4;
            out.writeShort((short) pkgLength);
            out.writeByte(IliveIdentifier.START_BYTE);
            out.writeBytes(responseBytes);
            out.writeByte(IliveIdentifier.END_BYTE);
        }
    }
}
