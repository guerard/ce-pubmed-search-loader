package com.conceptualeyes.pubmed.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SingleValueSerializer extends StdSerializer<Object> {

    public static final SingleValueSerializer instance = new SingleValueSerializer();

    public SingleValueSerializer() {
        this(null);
    }

    private SingleValueSerializer(Class<Object> t) {
        super(t);
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        try {
            Method method = value.getClass().getDeclaredMethod("getvalue");
            gen.writeString((String) method.invoke(value));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }
}
