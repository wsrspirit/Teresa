package com.tencent.teresa.codec.udp;

import com.tencent.teresa.codec.IoPacket;
import com.tencent.teresa.codec.IoPacketEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

public class UdpEncoderAdapter extends MessageToMessageEncoder<IoPacket> {
    private static final Logger logger = LoggerFactory.getLogger(UdpEncoderAdapter.class);
    private IoPacketEncoder encoder;

    public UdpEncoderAdapter(IoPacketEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, IoPacket msg, List<Object> out) throws Exception {
        InetSocketAddress addr = msg.getRouterAddr();
        if (addr == null){
            throw new IllegalStateException("IoPacket.getSenderAddr() could not be null for udp, check your IoPacket.newResponsePacket() method");
        }
        ByteBuf buf = null;
        boolean release = true;
        try {
            buf = ctx.alloc().directBuffer();
            encoder.doEncode(ctx, msg, buf);
            release = false;
        }finally{
            if(release && buf != null){
                buf.release();
            }
        }
        DatagramPacket pkg = new DatagramPacket(buf, addr);
        out.add(pkg);
    }
}
