package com.spirit.teresa.codec;

import java.net.InetSocketAddress;

public abstract class AbstractIoPacket<T extends IoPacket> implements IoPacket<T>{
    protected Long seq;
    protected Object cmd;
    protected Object subCmd;
    protected Long createTime;
    protected Object routerId;
    protected InetSocketAddress routerAddr;
    protected int retCode;
    protected String errMsg;
    protected Object content;


    @Override
    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    @Override
    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }

    @Override
    public Object getCmd() {
        return cmd;
    }

    public void setCmd(Object cmd) {
        this.cmd = cmd;
    }

    @Override
    public long getCreateTime() {
        return createTime != null ? createTime : 0;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Override
    public Object getRouterId() {
        return routerId;
    }

    public void setRouterId(Object routerId) {
        this.routerId = routerId;
    }

    @Override
    public InetSocketAddress getRouterAddr() {
        return routerAddr;
    }

    @Override
    public void setRouterAddr(InetSocketAddress routerAddr) {
        this.routerAddr = routerAddr;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    @Override
    public Object getSubCmd() {
        return subCmd;
    }

    public void setSubCmd(Object subCmd) {
        this.subCmd = subCmd;
    }
}
