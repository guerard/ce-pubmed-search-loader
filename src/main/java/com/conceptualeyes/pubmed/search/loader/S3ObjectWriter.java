package com.conceptualeyes.pubmed.search.loader;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.util.Assert;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class S3ObjectWriter implements ItemWriter<S3ObjectWriter.InputObject> {
    private static final Logger log = LoggerFactory.getLogger(S3ObjectWriter.class);


    AmazonS3 s3Client;
    String bucketName;

    @Override
    public void write(List<? extends S3ObjectWriter.InputObject> items) throws Exception {
        Assert.hasText(bucketName, "bucketName must be set!");
        log.info("Writing objects to S3: " + items);
        items.parallelStream().forEach(input -> s3Client.putObject(
                bucketName,
                input.key,
                new ByteArrayInputStream(input.byteArray),
                new ObjectMetadata() {{
                    setContentLength(input.byteArray.length);
                }}
        ));
    }

    static final class InputObject {
        final String key;
        final byte[] byteArray;

        InputObject(String key, byte[] byteArray) {
            this.key = key;
            this.byteArray = byteArray;
        }

        @Override
        public String toString() {
            return "{" +
                    "key='" + key + '\'' +
                    ", byteArray.length=" + String.valueOf(byteArray.length) +
                    '}';
        }
    }
}
