package com.tencent.teresa.exception;

public class RpcTimeoutException extends Exception {
    public RpcTimeoutException(String message) {
        super(message);
    }
}
