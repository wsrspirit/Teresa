package com.spirit.teresa.codec.udp;

import com.spirit.teresa.codec.IoPacket;
import com.spirit.teresa.codec.IoPacketDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class UdpDecoderAdapter extends MessageToMessageDecoder<DatagramPacket> {
    private static final Logger logger = LoggerFactory.getLogger(UdpDecoderAdapter.class);
    private IoPacketDecoder decoder;

    public UdpDecoderAdapter(IoPacketDecoder decoder) {
        this.decoder = decoder;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) throws Exception {
        ByteBuf buf = msg.content();
        List<Object> yetOut = new ArrayList<Object>(1);
        try {
            decoder.doDecode(ctx, buf, yetOut);
        }
        finally {
            if (ctx!=null && ctx.channel()!=null && !ctx.channel().isOpen()){
                logger.error("udp server/client channel should not be closed! check your decoder " + (decoder == null ? "null" : decoder.getClass().getName()));
            }
        }
        for (Object pkg : yetOut){
            if (pkg instanceof IoPacket) {
                ((IoPacket) pkg).setRouterAddr(msg.sender());
            }
            out.add(pkg);
        }
    }
}
