package com.conceptualeyes.pubmed.search.loader;

import com.conceptualeyes.pubmed.models.PubmedArticle;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

class PubMedArticlesS3Processor implements ItemProcessor<PubMedArticles, S3ObjectWriter.InputObject> {
    private static final Logger log = LoggerFactory.getLogger(PubMedArticlesS3Processor.class);

    ObjectWriter objectWriter;

    @Override
    public S3ObjectWriter.InputObject process(PubMedArticles pubMedArticles) throws Exception {
        log.info("Converting " + pubMedArticles + " to compressed JSON");
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             OutputStream os = new GZIPOutputStream(byteArrayOutputStream)) {
            int index = 0;
            final int lastIndex = pubMedArticles.list.size() - 1;
            os.write('[');
            for (PubmedArticle article : pubMedArticles.list) {
                os.write(objectWriter.writeValueAsBytes(article));
                if (index != lastIndex) {
                    os.write(',');
                }
                index++;
            }
            os.write(']');
            os.close();
            return new S3ObjectWriter.InputObject(
                    pubMedArticles.name + ".json.gz",
                    byteArrayOutputStream.toByteArray());
        }
    }
}
