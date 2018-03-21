package com.conceptualeyes.pubmed.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.List;

public class DistinctElementsListSerializer extends StdSerializer<List> {

    public static final DistinctElementsListSerializer instance = new DistinctElementsListSerializer();

    public DistinctElementsListSerializer() {
        this(null);
    }

    public DistinctElementsListSerializer(Class<List> t) {
        super(t);
    }

    @Override
    public void serialize(List value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        for (Object o : value) {
            char classNameChars[] = o.getClass().getSimpleName().toCharArray();
            classNameChars[0] = Character.toLowerCase(classNameChars[0]);
            gen.writeObjectField(new String(classNameChars), o);
        }
        gen.writeEndObject();
    }
}
