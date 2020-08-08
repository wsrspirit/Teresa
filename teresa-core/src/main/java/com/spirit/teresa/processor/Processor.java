package com.spirit.teresa.processor;


import com.spirit.teresa.codec.IoPacket;
import io.netty.channel.Channel;


/**
 * @param <T_REQ> 网络接收请求包，server是请求，client是响应
 * @param <T_RSP> 处理结果，server是响应结果，client是请求
 */
public interface Processor<T_REQ extends IoPacket, T_RSP extends IoPacket> {
	T_RSP process(T_REQ req, Channel ioChannel);
}
