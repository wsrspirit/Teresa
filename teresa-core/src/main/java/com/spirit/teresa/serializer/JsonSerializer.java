package com.spirit.teresa.serializer;

import com.dyuproject.protostuff.LinkedBuffer;

public class JsonSerializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) {
        return new byte[0];
    }

    @Override
    public byte[] serialize(Object obj, LinkedBuffer linkedBuffer) {
        return new byte[0];
    }

    @Override
    public <T> T deserialize(Class<T> klass, byte[] data) {
        return null;
    }

    @Override
    public String serializeToString(Object obj) {
        return null;
    }

    @Override
    public <T> T deserializeFromString(Class<T> klass, String data) {
        return null;
    }
}
