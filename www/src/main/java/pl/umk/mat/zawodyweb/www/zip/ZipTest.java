/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.www.zip;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import pl.umk.mat.zawodyweb.database.pojo.Tests;
import pl.umk.mat.zawodyweb.database.xml.Test;

/**
 *
 * @author faramir
 */
public class ZipTest {

    static Test addTest(ZipOutputStream out, Test xmlTest, Tests test) throws IOException {
        out.nextTest();

        ZipEntry entry;

        String inputFile = String.format("in%03d.txt", out.getTest());
        entry = new ZipEntry(inputFile);
        out.putNextEntry(entry);
        out.write(test.getInput().getBytes(StandardCharsets.UTF_8));
        out.closeEntry();

        String outputFile = String.format("out%03d.txt", out.getTest());
        entry = new ZipEntry(outputFile);
        out.putNextEntry(entry);
        out.write(test.getOutput().getBytes(StandardCharsets.UTF_8));
        out.closeEntry();

        xmlTest.setInput(inputFile);
        xmlTest.setOutput(outputFile);
        xmlTest.setMaxpoints(test.getMaxpoints());
        xmlTest.setOrder(test.getTestorder());
        xmlTest.setConfig(test.getConfig());
        xmlTest.setTimelimit(test.getTimelimit());

        return xmlTest;
    }

    static Tests getTest(ZipInputStream in, Test xmlTest) throws FileNotFoundException {
        String infile = xmlTest.getInput();
        String outfile = xmlTest.getOutput();

        if (infile == null || infile.isEmpty()) {
            throw new FileNotFoundException("Input file not found in zip archive!");
        }

        if (outfile == null || outfile.isEmpty()) {
            throw new FileNotFoundException("Output file not found in zip archive!");
        }

        if (!in.containsFile(infile)) {
            throw new FileNotFoundException("Input file not found in zip archive: " + infile);
        }

        if (!in.containsFile(outfile)) {
            throw new FileNotFoundException("Output file not found in zip archive: " + outfile);
        }

        Tests tests = new Tests();
        tests.setInput(new String(in.getFile(infile), StandardCharsets.UTF_8));
        tests.setOutput(new String(in.getFile(outfile), StandardCharsets.UTF_8));
        tests.setMaxpoints(xmlTest.getMaxpoints());
        tests.setTestorder(xmlTest.getOrder());
        tests.setTimelimit(xmlTest.getTimelimit());
        tests.setConfig(xmlTest.getConfig());
        tests.setVisibility(1); // FIXME: należy wartość pobrać z pliku XML (brak)

        return tests;
    }
}
