package com.clearbox;

import gov.uspto.bulkdata.BulkReaderArguments;
import gov.uspto.bulkdata.RecordProcessor;
import gov.uspto.bulkdata.RecordReader;
import gov.uspto.bulkdata.tools.grep.DocumentException;
import gov.uspto.patent.PatentReaderException;
import gov.uspto.patent.model.Patent;
import gov.uspto.patent.serialize.DocumentBuilder;
import gov.uspto.patent.serialize.JsonMapperStream;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.clearbox.USPTO2POM;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

public class Main {

    public static void run_single(Path inputBulkFile) throws PatentReaderException, IOException, DocumentException  {
        Path outputFile = Paths.get("example.txt");

        BulkReaderArguments config = new BulkReaderArguments();
        // config.parseArgs(args); // parse command-line args.
        config.setInputFile(inputBulkFile);
        config.setRecordReadLimit(1);
        config.setOutputFile(outputFile);

        RecordReader bulkReader = new RecordReader(config);

        DocumentBuilder<Patent> jsonBuilder = new JsonMapperStream(true);
        // DocumentBuilder<Patent> jsonBuilder = new JsonMapperFlat(true, false);
        // DocumentBuilder<Patent> jsonBuilder = new JsonMapperPATFT(true, false);

        RecordProcessor process = new USPTO2POM(jsonBuilder);
        bulkReader.read(process);
        //System.out.println("process complete...");
    }

    public static void main(String[] args) throws PatentReaderException, IOException, DocumentException {

        Logger.getLogger("gov.uspto.patent.model.classification").setLevel(Level.OFF);
        Logger.getLogger("gov.uspto").setLevel(Level.OFF);
        Logger.getRootLogger().setLevel(Level.OFF);

        //Enter data using BufferReader
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));

        // Reading data using readLine
        String tmp;
        while ((tmp = reader.readLine()) != null) {
           //System.out.println(tmp);
           //System.out.flush();
           run_single(Paths.get(tmp));
           System.out.println("done.");
           //System.out.flush();
        }

    }
}
