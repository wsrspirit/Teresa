package com.spirit.teresa.serializer;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import java.util.HashMap;
import java.util.Map;

public class ProtobufSerializer implements Serializer {
    private Map<Class, Schema> cachedSchemas = new HashMap<Class, Schema>();
    private int optimalBufferSize = 1024 * 512;
    private ThreadLocal<LinkedBuffer> linkedBuffer = ThreadLocal.withInitial(() -> LinkedBuffer.allocate(optimalBufferSize));

    public ProtobufSerializer() {
    }

    public ProtobufSerializer(int optimalBufferSize) {
        this.optimalBufferSize = optimalBufferSize;
    }

    @Override
    public byte[] serialize(Object obj) {
        LinkedBuffer linkedBuffer = this.linkedBuffer.get();
        Schema schema = getSchema(obj.getClass());
        try {
            return ProtostuffIOUtil.toByteArray(obj,schema,linkedBuffer);
        } finally {
            linkedBuffer.clear();
        }
    }

    //这里需要传入linkedBuffer是为了让channelBased使用，默认使用ThreadLocal中的linkedBuffer
    @Override
    public byte[] serialize(Object obj, LinkedBuffer linkedBuffer) {
        Schema schema = getSchema(obj.getClass());
        try {
            return ProtostuffIOUtil.toByteArray(obj,schema,linkedBuffer);
        } finally {
            linkedBuffer.clear();
        }
    }

    @Override
    public <T> T deserialize(Class<T> klass, byte[] data) {
        Schema schema = getSchema(klass);
        try {
            T obj = klass.newInstance();
            ProtostuffIOUtil.mergeFrom(data, obj, schema);
            return obj;
        } catch(Exception e) {
            throw new RuntimeException( "deserialize error:" + e.getMessage(), e);
        }
    }

    private Schema getSchema(Class klass) {
        Schema schema = this.cachedSchemas.get(klass);
        if(schema == null) {
            schema = RuntimeSchema.getSchema(klass);
            this.cachedSchemas.put(klass, schema);
        }

        return schema;
    }

}
