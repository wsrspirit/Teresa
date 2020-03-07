package com.spirit.teresa.serializer;

import com.dyuproject.protostuff.LinkedBuffer;

public class JsonSerializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) throws Exception {
        return new byte[0];
    }

    @Override
    public byte[] serialize(Object obj, LinkedBuffer linkedBuffer) throws Exception {
        return new byte[0];
    }

    @Override
    public <T> T deserialize(Class<T> klass, byte[] data) throws Exception {
        return null;
    }

    @Override
    public String serializeToString(Object obj) throws Exception {
        return null;
    }

    @Override
    public byte[] serializeToString(Object obj, LinkedBuffer linkedBuffer) throws Exception {
        return new byte[0];
    }

    @Override
    public <T> T deserializeFromString(Class<T> klass, String data) throws Exception {
        return null;
    }
}
