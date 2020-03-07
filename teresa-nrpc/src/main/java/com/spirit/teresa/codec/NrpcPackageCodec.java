package com.spirit.teresa.codec;

import com.spirit.teresa.serializer.Serializer;
import io.netty.channel.Channel;

public class NrpcPackageCodec implements IoPacketCodec<IoPacket> {
    protected boolean client = false;
    protected Serializer serializer;

    public NrpcPackageCodec() {
    }

    public NrpcPackageCodec(Serializer serializer) {
        this.serializer = serializer;
    }

    public NrpcPackageCodec(boolean client, Serializer serializer) {
        this.client = client;
        this.serializer = serializer;
    }

    @Override
    public IoPacketDecoder getDecoder(Channel ch) {
        return new NrpcPackageDecoder(client,serializer);
    }

    @Override
    public IoPacketEncoder<IoPacket> getEncoder(Channel ch) {
        return new NrpcPackageEncoder(client,serializer);
    }

    public boolean getClient() {
        return client;
    }

    public void setClient(boolean client) {
        this.client = client;
    }

    public Serializer getSerializer() {
        return serializer;
    }

    public void setSerializer(Serializer serializer) {
        this.serializer = serializer;
    }
}
