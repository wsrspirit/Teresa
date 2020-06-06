package com.spirit.teresa.exception;

import com.spirit.teresa.utils.ErrorCode;

public class TeresaException extends RuntimeException {
    private int code;

    public TeresaException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public TeresaException(ErrorCode errorCode) {
        this(errorCode.getErrCode(),errorCode.getErrMsg());
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
