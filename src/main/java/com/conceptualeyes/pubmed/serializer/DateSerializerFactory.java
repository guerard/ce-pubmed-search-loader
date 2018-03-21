package com.conceptualeyes.pubmed.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class DateSerializerFactory {

    public static final DateSerializerFactory instance = new DateSerializerFactory();

    public interface DatePartsAccessor<T> {
        String getYear(T object);

        String getMonth(T object);

        String getDay(T object);
    }

    public <T> StdSerializer<T> newSerializer(final DatePartsAccessor<T> datePartsAccessor) {
        return new StdSerializer<T>((Class<T>) null) {
            @Override
            public void serialize(T value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeString(
                        DateUtils.YYYY_MM_DD_dateString(
                                datePartsAccessor.getYear(value),
                                datePartsAccessor.getMonth(value),
                                datePartsAccessor.getDay(value)));
            }
        };
    }
}
