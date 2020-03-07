package com.spirit.teresa.codec;

import io.netty.channel.Channel;

public interface IoPacketCodec<T_RSP extends IoPacket> {
    IoPacketDecoder getDecoder(Channel ch);
    IoPacketEncoder<T_RSP> getEncoder(Channel ch);
}
