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

	int getRetCode();

	String getErrMsg();

	/**
	 * fixme should not throw checked exception
	 * @param reqPacket
	 * @param ec
	 * @param message
	 * @param body
	 * @param serializer
	 * @return
	 * @throws Exception
	 */
	T newResponsePacket(T reqPacket, int ec, String message, Object body, Serializer serializer);

	Object getContent(Class clazz, Serializer serializer);

	void setContent(Object content,Serializer serializer);

	boolean isRequest();

	void setRequest(boolean isRequest);

}
