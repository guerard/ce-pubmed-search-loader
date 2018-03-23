# Parse PubMed XML into JSON, and upload to S3
A Spring Boot application for parsing PubMed XML files on the local filesystem
into JSON, and then uploading them to a bucket in S3 as gzipped data.

## Prepare your environment
The application assumes you have a directory that mirrors the strucutre of the PubMed FTP
filesystem servers (more info here: ftp://ftp.ncbi.nlm.nih.gov/pubmed/baseline/README.txt ).
The easiest way to get this working is to rsync a local directory to the pubmed baseline (or updates)
directory, as outlined here http://fnl.es/medline-kung-fu.html .

## Prepare S3
The current application hardcodes the bucket name, so ensure that your bucket `pubmed-json` is
created, and that you're passing access credentials in a way that the DefaultAWSCredentialsProviderChain
can pick them up.

## Running
This project uses gradle for the building and dependency management.

To build the jar, run: `./gradlew bootJar`

To run the application, run: `java -Dfile.encoding=UTF-8 -jar <path-to-jar> importPubMedXmlAsJson rootDir=<path-to-pub-med-files> startId=<begin-index> endId=<end-index>`

`rootDir` is the path to the root directory containing the XML files to parse. If you notice, each file has an integer ID 
(e.g. `pubmed18n0463.xml.gz` has an ID of 463). `startId` is the first ID to process and `endId` is the last ID (inclusive).
The application will parse and upload all the XML files in between `startId` and `endId`. `importPubMedXmlAsJson` is just
the name of the job, and can be omitted now because it's also the default (however that might change in the future).
