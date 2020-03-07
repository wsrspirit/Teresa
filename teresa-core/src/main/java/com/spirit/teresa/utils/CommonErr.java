package com.spirit.teresa.utils;

public enum CommonErr {
    PACKET_OVER_FLOW(40000,"packet over flow"),
    SERVER_INNER_ERR(50000,"server inner err");

    public int errCode;
    public String errMsg;

    CommonErr(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

}
