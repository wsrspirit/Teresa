package com.spirit.teresa.serializer;

import com.dyuproject.protostuff.LinkedBuffer;


public interface Serializer {

    //for Binary
    byte[] serialize(Object obj);

    byte[] serialize(Object obj, LinkedBuffer linkedBuffer);

    <T> T deserialize(Class<T> klass, byte[] data);

    //for Json
    String serializeToString(Object obj);

    <T> T deserializeFromString(Class<T> klass, String data);
}
