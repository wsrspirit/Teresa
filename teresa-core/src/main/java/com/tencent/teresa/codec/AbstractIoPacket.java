package com.tencent.teresa.codec;

import java.net.InetSocketAddress;

public abstract class AbstractIoPacket<T extends IoPacket> implements IoPacket<T>{
    protected Long seq;
    protected Object cmd;
    protected Object subcmd;
    protected Long createTime;
    protected Object routerId;
    protected InetSocketAddress routerAddr;
    protected Integer errCode;
    protected String errMsg;
    protected Integer estimateSize;
    protected Object content;


    @Override
    public long getRetCode() {
        return 0;
    }

    @Override
    public String getErrorMsg() {
        return null;
    }

    @Override
    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
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

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    @Override
    public int getEstimateSize() {
        return estimateSize != null ? estimateSize : 0;
    }

    public void setEstimateSize(Integer estimateSize) {
        this.estimateSize = estimateSize;
    }

    @Override
    public Object getSubcmd() {
        return subcmd;
    }

    public void setSubcmd(Object subcmd) {
        this.subcmd = subcmd;
    }


}
