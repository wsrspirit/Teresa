package com.spirit.teresa.codec;

import com.spirit.teresa.codec.base.BaseDecoder;
import com.spirit.teresa.packet.NrpcPacket;
import com.spirit.teresa.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class NrpcPackageDecoder extends BaseDecoder {
    private boolean isClient;
    private static final Logger logger = LoggerFactory.getLogger(NrpcPackageDecoder.class);
    public NrpcPackageDecoder(boolean isClient,Serializer serializer) {
        super(serializer);
        this.isClient = isClient;
    }
    @Override
    protected void decode0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() < 5) {
            return;
        }
        byteBuf.markReaderIndex();

        final byte start = byteBuf.readByte();
        if (start != NrpcPacket.START_BYTE) {
            byteBuf.clear();
            if (channelHandlerContext != null) {
                channelHandlerContext.channel().close();
            }
            return;
        }

        int pkgLength;
        //长度开头
        pkgLength = byteBuf.readInt();
        logger.debug("NrpcDecoder decode lenght {}",pkgLength);

        if (byteBuf.readableBytes() < pkgLength - 5) {
            byteBuf.resetReaderIndex();
            return;
        }

        NrpcPacket packet = null;

        if (pkgLength > 0) {
            byte[] bytes = new byte[pkgLength - 6];
            byteBuf.readBytes(bytes);

            try {
                packet = serializer.deserialize(NrpcPacket.class,bytes);
            } catch (Exception e) {
                logger.error("decode err pkg length " + pkgLength,e);
                byteBuf.clear();
                if (channelHandlerContext != null) {
                    channelHandlerContext.channel().close();
                }
                return;
            }
        }

        if (byteBuf.readByte() != NrpcPacket.END_BYTE) {
            byteBuf.clear();
            if (channelHandlerContext != null) {
                channelHandlerContext.channel().close();
            }
            return;
        }
        list.add(packet);
    }
}
