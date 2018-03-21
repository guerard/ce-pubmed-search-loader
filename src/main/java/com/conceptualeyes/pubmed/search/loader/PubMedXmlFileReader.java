package com.conceptualeyes.pubmed.search.loader;

import com.conceptualeyes.pubmed.models.PubmedArticle;
import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.core.io.InputStreamResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Locale;
import java.util.zip.GZIPInputStream;

class PubMedXmlFileReader extends AbstractItemCountingItemStreamItemReader<PubMedArticles> {

    private static final Logger log = LoggerFactory.getLogger(PubMedXmlFileReader.class);
    private static final String PUB_MED_ID_FORMAT = "pubmed18n%04d";
    private static final String PUB_MED_XML_FILE_SUFFIX = ".xml.gz";
    private static ThreadLocal<Jaxb2Marshaller> jaxb2Marshaller = ThreadLocal.withInitial(
            () -> new Jaxb2Marshaller() {{
                setContextPath("com.conceptualeyes.pubmed.models");
            }}
    );
    private final StaxEventItemReader<PubmedArticle> staxEventItemReader;
    private final File rootDir;

    PubMedXmlFileReader(
            StaxEventItemReader<PubmedArticle> staxEventItemReader,
            File rootDir,
            String startId,
            String endId) {
        staxEventItemReader.setFragmentRootElementName("PubmedArticle");
        staxEventItemReader.setUnmarshaller(jaxb2Marshaller());
        setName("PubMedXmlFileReader");
        setCurrentItemCount(Integer.valueOf(startId) - 1);
        setMaxItemCount(Integer.valueOf(endId));
        this.staxEventItemReader = staxEventItemReader;
        this.rootDir = rootDir;
    }

    private static Jaxb2Marshaller jaxb2Marshaller() {
        return jaxb2Marshaller.get();
    }

    private static String pubMedArticlesId(final int id) {
        return String.format(Locale.US, PUB_MED_ID_FORMAT, id);
    }

    @Override
    protected PubMedArticles doRead() throws Exception {
        String pubMedArticlesId = pubMedArticlesId(getCurrentItemCount());
        log.info("Importing PubMed articles from XML file: " + pubMedArticlesId);
        staxEventItemReader.setResource(
                new InputStreamResource(
                        new BufferedInputStream(
                                new GZIPInputStream(
                                        new FileInputStream(
                                                new File(rootDir, pubMedArticlesId + PUB_MED_XML_FILE_SUFFIX))),
                                /* 1024^2 = 1 M */1048576)));
        try {
            staxEventItemReader.open(new ExecutionContext());
            ImmutableList.Builder<PubmedArticle> outList = ImmutableList.builder();
            PubmedArticle article;
            while ((article = staxEventItemReader.read()) != null) {
                outList.add(article);
            }
            return new PubMedArticles(pubMedArticlesId, outList.build());
        } finally {
            staxEventItemReader.close();
        }
    }

    @Override
    protected void jumpToItem(int itemIndex) throws Exception {
        setCurrentItemCount(itemIndex);
    }

    @Override
    protected void doOpen() throws Exception {
    }

    @Override
    protected void doClose() throws Exception {
    }
}
