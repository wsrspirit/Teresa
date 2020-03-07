package com.spirit.teresa.codec;

import com.spirit.teresa.codec.base.BaseDecoder;
import com.spirit.teresa.packet.IliveIdentifier;
import com.spirit.teresa.packet.ILiveRequest;
import com.spirit.teresa.packet.ILiveResponse;
import com.spirit.teresa.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ILivePackageDecoder extends BaseDecoder {
    private boolean isClient;
    private static final Logger logger = LoggerFactory.getLogger(ILivePackageDecoder.class);
    public ILivePackageDecoder(boolean isClient,Serializer serializer) {
        super(serializer);
        this.isClient = isClient;
    }

    @Override
    protected void decode0(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }
        in.markReaderIndex();
        final int pkgLength;
        //长度开头
        pkgLength = in.readUnsignedShort();
        if (pkgLength < 4) {
            in.clear();
            if (ctx != null) {
                ctx.channel().close();
            }
            return;
        }
        final byte start = in.readByte();
        if (start != IliveIdentifier.START_BYTE) {
            in.clear();
            if (ctx != null) {
                ctx.channel().close();
            }
            return;
        }

        if (in.readableBytes() < pkgLength - 3) {
            in.resetReaderIndex();
            return;
        }

        AbstractIoPacket ioPacket = null;
        if (pkgLength > 4) {
            byte[] bytes = new byte[pkgLength - 4];
            in.readBytes(bytes);

            if (isClient) {
                ILiveResponse response = null;
                try {
                    response = serializer.deserialize(ILiveResponse.class,bytes);
                } catch (Exception e) {
                    logger.error("client mode deserialize err {}",e);
                    in.clear();
                    if (ctx != null) {
                        ctx.channel().close();
                    }
                    return;
                }
                ioPacket = response;
//                ilive.setiLiveResponse(response);

            } else {
                ILiveRequest request = null;
                try {
                    request = serializer.deserialize(ILiveRequest.class,bytes);
                } catch (Exception e) {
                    logger.error("server mode deserialize err {}",e);
                    in.clear();
                    if (ctx != null) {
                        ctx.channel().close();
                    }
                    return;
                }
                ioPacket = request;
//                ilive.setiLiveRequest(request);
            }

        }
        if (in.readByte() != IliveIdentifier.END_BYTE) {
            in.clear();
            if (ctx != null) {
                ctx.channel().close();
            }
            return;
        }
        out.add(ioPacket);
    }
}
