package com.conceptualeyes.pubmed.search.loader;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.conceptualeyes.pubmed.serializer.PubmedObjectMapperSupplier;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.File;

@Configuration
@EnableBatchProcessing
class BatchConfiguration {
    @Autowired
    JobBuilderFactory jobs;

    @Autowired
    StepBuilderFactory steps;

    @Autowired
    DataSource dataSource;

    @Bean
    Job importPubMedXmlAsJson(Step xmlToJsonStep, JobCompletionNotificationListener listener) {
        return jobs.get("importPubMedXmlAsJson")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(xmlToJsonStep)
                .end()
                .build();
    }

    @Bean
    ObjectWriter objectWriter() {
        return new PubmedObjectMapperSupplier().get().writer();
    }

    @Bean
    PubMedArticlesS3Processor pubMedArticlesProcessor(ObjectWriter writer) {
        return new PubMedArticlesS3Processor() {{
            this.objectWriter = writer;
        }};
    }

    @Bean
    @StepScope
    PubMedXmlFileReader pubMedXmlFileReader(
            @Value("#{jobParameters[rootDir]}") String rootDir,
            @Value("#{jobParameters[startId]}") String startId,
            @Value("#{jobParameters[endId]}") String endId) {
        return new PubMedXmlFileReader(
                new StaxEventItemReader<>(),
                new File(rootDir),
                startId,
                endId);
    }

    @Bean
    S3ObjectWriter s3ObjectWriter() {
        return new S3ObjectWriter() {{
            s3Client = AmazonS3Client.builder()
                    .withRegion(Regions.US_EAST_1)
                    .withCredentials(
                            new DefaultAWSCredentialsProviderChain())
                    .withClientConfiguration(
                            new ClientConfiguration()
                                    .withRequestTimeout(120_000))
                    .build();
            bucketName = "pubmed-json";
        }};
    }

    @Bean
    Step xmlToJsonStep(
            PubMedXmlFileReader reader,
            PubMedArticlesS3Processor processor,
            S3ObjectWriter writer) {
        return steps.get("xmlToJsonStep")
                .<PubMedArticles, S3ObjectWriter.InputObject>chunk(3)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
