package com.spirit.teresa.codec;

import com.spirit.teresa.serializer.Serializer;

import java.net.InetSocketAddress;

public interface IoPacket<T extends IoPacket> {

	Object getSeq();

	Object getCmd();

	Object getSubcmd();

	long getCreateTime();

	Object getRouterId();

	InetSocketAddress getRouterAddr();

	void setRouterAddr(InetSocketAddress addr);

	int getEstimateSize();

	long getRetCode();

	String getErrorMsg();

	T newResponsePacket(T reqPacket, int ec, String message, Object body, Serializer serializer) throws Exception;

	Object getContent(Class clazz, Serializer serializer) throws Exception;

	void setContent(Object content,Serializer serializer) throws Exception;

}
