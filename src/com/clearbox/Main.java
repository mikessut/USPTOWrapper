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

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

public class Main {

    public static void main(String[] args) throws PatentReaderException, IOException, DocumentException {
        //System.out.println("Here I am!!");

        Logger.getRootLogger().setLevel(Level.OFF);

        //Enter data using BufferReader
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));

        // Reading data using readLine
        String filename = reader.readLine();
        String buffer = "";
        String tmp;
        while ((tmp = reader.readLine()) != null) {
           buffer += tmp + "\r\n";
        }
        // Printing the read line
        //System.out.println(buffer);
        String[] fileparts = filename.split("\\.");
        //System.out.println(fileparts[0] + " " + fileparts[1]);
        File temp = File.createTempFile(fileparts[0], "." + fileparts[1]);
        //File temp = new File("pg.sgm");
        //File temp = new File("/trash/pg011218_01.sgm");
        BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
        bw.write(buffer);
        bw.close();

        BulkReaderArguments config = new BulkReaderArguments();
        // config.parseArgs(args); // parse command-line args.
        config.setInputFile(temp.toPath());
        config.setRecordReadLimit(1);
        //config.setOutputFile(outputFile);

        RecordReader bulkReader = new RecordReader(config);

        DocumentBuilder<Patent> jsonBuilder = new JsonMapperStream(true);
        RecordProcessor process = new USPTO2POM(jsonBuilder);
        Path output = null;  // Write to STDOUT
        bulkReader.read(temp, process, output);
        /*
        Path inputBulkFile = Paths.get(args[0]);
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
        */

    }
}
