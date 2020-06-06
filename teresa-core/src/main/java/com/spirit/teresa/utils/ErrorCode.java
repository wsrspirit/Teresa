package com.spirit.teresa.utils;

public enum ErrorCode {
    SUCCESS(0,""),
    PACKET_OVER_FLOW(40000,"packet over flow"),
    SERVER_INNER_ERR(50000,"server inner err")
    ;

    public int errCode;
    public String errMsg;

    ErrorCode(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }


    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
