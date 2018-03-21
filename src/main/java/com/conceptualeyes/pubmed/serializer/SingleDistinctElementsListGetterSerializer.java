package com.conceptualeyes.pubmed.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class SingleDistinctElementsListGetterSerializer extends StdSerializer<Object> {

    public static final SingleDistinctElementsListGetterSerializer instance =
            new SingleDistinctElementsListGetterSerializer();

    public SingleDistinctElementsListGetterSerializer() {
        this(null);
    }

    private SingleDistinctElementsListGetterSerializer(Class<Object> t) {
        super(t);
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        try {
            Method method = value.getClass().getDeclaredMethods()[0];
            DistinctElementsListSerializer.instance.serialize((List) method.invoke(value), gen, provider);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }
}
