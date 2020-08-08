package com.spirit.teresa.codec;

import com.spirit.teresa.serializer.Serializer;

import java.net.InetSocketAddress;

/**
 * tcp层的包结构
 * @param <T>
 */
public interface IoPacket<T extends IoPacket> {

	long getSeq();

	void setSeq(long seq);

	Object getCmd();

	void setCmd(Object cmd);

	Object getSubCmd();

	void setSubCmd(Object subCmd);

	long getCreateTime();

	Object getRouterId();

	InetSocketAddress getRouterAddr();

	void setRouterAddr(InetSocketAddress addr);

	int getRetCode();

	void setRetCode(int retCode);

	String getErrMsg();

	void setErrMsg(String errMsg);

	T newResponsePacket(T reqPacket, int ec, String message, Object body, Serializer serializer);

	void setBizContentBytes(byte[] bizContentBytes);

	byte[] getBizContentBytes();

	boolean isRequest();

	void setRequest(boolean isRequest);

	String toString();

}
