package com.spirit.teresa.route;

import java.net.InetSocketAddress;

public class RouterInfo {
    private final String ip;
    private final int port;
    private InetSocketAddress socketAddress;

    public RouterInfo(String ip, int port) {
        this.ip = ip;
        this.port = port;
        socketAddress = new InetSocketAddress(ip,port);
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public InetSocketAddress getSocketAddress() {
        return socketAddress;
    }

    public void setSocketAddress(InetSocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }
}
