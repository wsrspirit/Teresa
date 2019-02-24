package com.tencent.teresa.utils;

//todo all this can be define by configuration
public class U {
    public static byte[] EMPTY_BYTES = new byte[0];
    public static int DEFAULT_PERMITS = 30000;
    public static int DEFAULT_THREADS = 16;
    public static final String TCP = "tcp";
    public static final String UDP = "udp";

    public static final String THREAD_WORKER = "thread";
    public static final String COROUTINE_WORKER = "coroutine";
    public static final String PB_SERIALIZER = "protobuf";
    public static final String JSON_SERIALIZER = "json";
}
