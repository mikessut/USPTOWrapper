package com.clearbox;

import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;

import gov.uspto.bulkdata.BulkReaderArguments;
import gov.uspto.bulkdata.RecordProcessor;
import gov.uspto.bulkdata.RecordReader;
import gov.uspto.bulkdata.tools.grep.DocumentException;
import gov.uspto.patent.PatentDocFormat;
import gov.uspto.patent.PatentReader;
import gov.uspto.patent.PatentReaderException;
import gov.uspto.patent.model.Patent;
import gov.uspto.patent.serialize.DocumentBuilder;
//import gov.uspto.patent.serialize.JsonMapperFlat;
//import gov.uspto.patent.serialize.JsonMapperPATFT;
import gov.uspto.patent.serialize.JsonMapperStream;

/*
Usage on python side:

from subprocess import Popen, PIPE, STDOUT
p = Popen(['java', '-cp', 'USPTOWrapper.jar', 'com.clearbox.Main'], stdout=PIPE, stdin=PIPE)
result = p.communicate(input=txt.encode())

 */

public class USPTO2POM implements RecordProcessor {

    private PatentReader patentReader;
    private DocumentBuilder<Patent> jsonBuilder;

    public USPTO2POM(DocumentBuilder<Patent> jsonBuilder) {
        this.jsonBuilder = jsonBuilder;
    }

    @Override
    public void setPatentDocFormat(PatentDocFormat docFormat) {
        this.patentReader = new PatentReader(docFormat);
    }

    @Override
    public void initialize(Writer writer) throws IOException {
        System.err.println("--- START ---");
    }

    @Override
    public Boolean process(String sourceTxt, String rawRecord, Writer writer) throws DocumentException, IOException {
        // writer.write(sourceTxt);
        // writer.write(rawRecord);

        try {
            Patent patent = patentReader.read(new StringReader(rawRecord));

            jsonBuilder.write(patent, writer);

        } catch (PatentReaderException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public void finish(Writer writer) throws IOException {
        System.err.println("--- DONE ---");
    }

}
