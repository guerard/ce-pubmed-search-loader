package com.conceptualeyes.pubmed.serializer;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.util.function.Supplier;

public class PubmedObjectMapperSupplier implements Supplier<ObjectMapper> {
    @Override
    public ObjectMapper get() {
        SimpleModule simpleModule = new SimpleModule();
        PubmedSerializersProvider.addSerializers(simpleModule);
        return new ObjectMapper()
                .setSerializationInclusion(Include.NON_NULL)
                .registerModule(simpleModule);
    }
}
