package com.conceptualeyes.pubmed.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class ListClassSerializer extends StdSerializer<Object> {

    public static final ListClassSerializer instance = new ListClassSerializer();

    public ListClassSerializer() {
        this(null);
    }

    public ListClassSerializer(Class<Object> t) {
        super(t);
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        try {
            gen.writeObject(
                    value.getClass().getDeclaredMethods()[0].invoke(value));
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }
}
