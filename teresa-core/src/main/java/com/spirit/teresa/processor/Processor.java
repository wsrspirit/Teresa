package com.spirit.teresa.processor;


import com.spirit.teresa.codec.IoPacket;
import io.netty.channel.Channel;


public interface Processor<T_REQ extends IoPacket, T_RSP extends IoPacket> {
	T_RSP process(T_REQ req, Channel ioChannel) throws Exception;
}
