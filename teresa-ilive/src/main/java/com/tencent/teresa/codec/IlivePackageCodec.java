package com.tencent.teresa.codec;

import io.netty.channel.Channel;

public class IlivePackageCodec implements IoPacketCodec<IoPacket> {

    protected boolean client = false;

    public IlivePackageCodec() {
    }

    public IlivePackageCodec(boolean client) {
        this.client = client;
    }

    @Override
    public IoPacketDecoder getDecoder(Channel ch) {
        return new ILivePackageDecoder(client);
    }

    @Override
    public IoPacketEncoder<IoPacket> getEncoder(Channel ch) {
        return new ILivePackageEncoder(client);
    }

    public boolean getClient() {
        return client;
    }

    public void setClient(boolean client) {
        this.client = client;
    }
}
