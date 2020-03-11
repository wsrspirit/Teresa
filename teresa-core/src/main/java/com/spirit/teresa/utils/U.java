package com.spirit.teresa.utils;

//todo all this can be define by configuration
public class U {
    public static byte[] EMPTY_BYTES = new byte[0];
    public static int DEFAULT_PERMITS = 30000;
    public static int DEFAULT_THREADS = 16;

    public static final String THREAD_WORKER = "thread";
    public static final String COROUTINE_WORKER = "coroutine";
    public static final String PB_SERIALIZER = "PROTOBUF_SERIALIZER";
    public static final String JSON_SERIALIZER = "JSON_SERIALIZER";

    //bean definition of Teresa
    public static final String WORKER_SERVICE_BEAN_NAME = "workerService";
    public static final String IO_PACKET_CODEC_BEAN_NAME = "ioPacketCodec";
    public static final String ROUTER_SERVICE_BEAN_NAME = "routerService";
    public static final String RPC_CLIENT_BEAN_NAME = "rpcClient";


}
