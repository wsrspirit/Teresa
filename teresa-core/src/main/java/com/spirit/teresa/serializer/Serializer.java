package com.spirit.teresa.serializer;

import com.dyuproject.protostuff.LinkedBuffer;


public interface Serializer {

    //for Binary
    byte[] serialize(Object obj) throws Exception;

    byte[] serialize(Object obj, LinkedBuffer linkedBuffer) throws Exception;

    <T> T deserialize(Class<T> klass, byte[] data) throws Exception;

    //for Json
    String serializeToString(Object obj) throws Exception;

    byte[] serializeToString(Object obj, LinkedBuffer linkedBuffer) throws Exception;

    <T> T deserializeFromString(Class<T> klass, String data) throws Exception;
}
