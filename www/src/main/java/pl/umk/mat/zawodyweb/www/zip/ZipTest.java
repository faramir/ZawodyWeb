/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.umk.mat.zawodyweb.www.zip;

import java.io.IOException;
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
        out.write(test.getInput().getBytes("UTF-8"));
        out.closeEntry();

        String outputFile = String.format("out%03d.txt", out.getTest());
        entry = new ZipEntry(outputFile);
        out.putNextEntry(entry);
        out.write(test.getOutput().getBytes("UTF-8"));
        out.closeEntry();

        xmlTest.setInput(inputFile);
        xmlTest.setOutput(outputFile);
        xmlTest.setMaxpoints(test.getMaxpoints());
        xmlTest.setOrder(test.getTestorder());
        xmlTest.setTimelimit(test.getTimelimit());

        return xmlTest;
    }
}
