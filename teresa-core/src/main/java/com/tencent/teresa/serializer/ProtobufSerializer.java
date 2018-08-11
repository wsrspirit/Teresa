package com.tencent.teresa.serializer;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import java.util.HashMap;
import java.util.Map;

public class ProtobufSerializer implements Serializer {
    private Map<Class, Schema> cachedSchemas = new HashMap<Class, Schema>();
    private int optimalBufferSize = 1024 * 512;
    private ThreadLocal<LinkedBuffer> linkedBuffer = new ThreadLocal<LinkedBuffer>() {
        @Override
        protected LinkedBuffer initialValue() {
            return LinkedBuffer.allocate(optimalBufferSize);
        }
    };

    public ProtobufSerializer() {
    }

    public ProtobufSerializer(int optimalBufferSize) {
        this.optimalBufferSize = optimalBufferSize;
    }

    @Override
    public byte[] serialize(Object obj) throws Exception {
        LinkedBuffer linkedBuffer = this.linkedBuffer.get();
        Schema schema = getSchema(obj.getClass());
        try {
            return ProtostuffIOUtil.toByteArray(obj,schema,linkedBuffer);
        }catch(Exception e) {
            throw new Exception("serialize error:" + e.getMessage(), e);
        } finally {
            linkedBuffer.clear();
        }
    }

    @Override
    public byte[] serialize(Object obj, LinkedBuffer linkedBuffer) throws Exception {
        Schema schema = getSchema(obj.getClass());
        try {
            return ProtostuffIOUtil.toByteArray(obj,schema,linkedBuffer);
        }catch(Exception e) {
            throw new Exception("serialize error:" + e.getMessage(), e);
        } finally {
            linkedBuffer.clear();
        }
    }

    @Override
    public <T> T deserialize(Class<T> klass, byte[] data) throws Exception {
        Schema schema = getSchema(klass);
        try {
            T obj = klass.newInstance();
            ProtobufIOUtil.mergeFrom(data, obj, schema);
            return obj;
        }catch(Exception e) {
            throw new Exception( "deserialize error:" + e.getMessage(), e);
        }
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


    private Schema getSchema(Class klass) throws Exception {

        Schema schema = this.cachedSchemas.get(klass);
        if(schema == null) {
            try {
                schema = RuntimeSchema.getSchema(klass);
            } catch (Exception e) {
                throw new Exception("instantiate class failed:" + e.getMessage(), e);
            }
            this.cachedSchemas.put(klass, schema);
        }

        return schema;
    }

}
