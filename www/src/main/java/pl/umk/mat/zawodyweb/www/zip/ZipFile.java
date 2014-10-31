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
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.zip.ZipEntry;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.io.output.ByteArrayOutputStream;
import pl.umk.mat.zawodyweb.database.pojo.*;
import pl.umk.mat.zawodyweb.database.xml.Contest;
import pl.umk.mat.zawodyweb.database.xml.Problem;
import pl.umk.mat.zawodyweb.database.xml.Serie;
import pl.umk.mat.zawodyweb.database.xml.Test;

/**
 *
 * @author faramir
 */
public class ZipFile {

    private static final XmlParser xmlParser = new XmlParser();

    private ZipFile() {
    }

    static XMLGregorianCalendar convert(Date date) {
        try {
            GregorianCalendar gregDate = (GregorianCalendar) GregorianCalendar.getInstance();
            gregDate.setTime(date);

            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregDate);
        } catch (Exception e) {
            return null;
        }
    }

    static Date convert(XMLGregorianCalendar xmlDate) {
        try {
            return xmlDate.toGregorianCalendar().getTime();
        } catch (Exception e) {
            return null;
        }
    }

    private static void putXml(ZipOutputStream out, String filename, Object xml) throws IOException, JAXBException {
        ZipEntry entry = new ZipEntry(filename);
        out.putNextEntry(entry);
        out.write(xmlParser.toString(xml).getBytes("UTF-8"));
        out.closeEntry();
    }

    public static byte[] zipContest(Contests contest) throws IOException, JAXBException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream out = new ZipOutputStream(baos);
        out.setMethod(ZipOutputStream.DEFLATED);
        out.setLevel(java.util.zip.Deflater.BEST_COMPRESSION);

        Contest xml = new Contest();
        ZipContest.addContest(out, xml, contest);
        putXml(out, "contest.xml", xml);

        out.close();

        return baos.toByteArray();
    }

    public static byte[] zipSerie(Series serie) throws IOException, JAXBException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream out = new ZipOutputStream(baos);
        out.setMethod(ZipOutputStream.DEFLATED);
        out.setLevel(java.util.zip.Deflater.BEST_COMPRESSION);

        Serie xml = new Serie();
        ZipSerie.addSerie(out, xml, serie);
        putXml(out, "serie.xml", xml);

        out.close();

        return baos.toByteArray();
    }

    public static byte[] zipProblem(Problems problem) throws IOException, JAXBException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream out = new ZipOutputStream(baos);
        out.setMethod(ZipOutputStream.DEFLATED);
        out.setLevel(java.util.zip.Deflater.BEST_COMPRESSION);

        Problem xml = new Problem();
        ZipProblem.addProblem(out, xml, problem);
        putXml(out, "problem.xml", xml);

        out.close();

        return baos.toByteArray();
    }

    public static byte[] zipTests(List<Tests> tests) throws IOException, JAXBException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream out = new ZipOutputStream(baos);
        out.setMethod(ZipOutputStream.DEFLATED);
        out.setLevel(java.util.zip.Deflater.BEST_COMPRESSION);

        pl.umk.mat.zawodyweb.database.xml.Tests xml = new pl.umk.mat.zawodyweb.database.xml.Tests();
        for (Tests test : tests) {
            Test xmlTest = new Test();
            ZipTest.addTest(out, xmlTest, test);
            xml.getTests().add(xmlTest);
        }
        putXml(out, "tests.xml", xml);

        out.close();

        return baos.toByteArray();
    }

    public static byte[] zipTest(Tests test) throws IOException, JAXBException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream out = new ZipOutputStream(baos);
        out.setMethod(ZipOutputStream.DEFLATED);
        out.setLevel(java.util.zip.Deflater.BEST_COMPRESSION);

        Test xml = new Test();
        ZipTest.addTest(out, xml, test);
        putXml(out, "test.xml", xml);

        out.close();

        return baos.toByteArray();
    }

    public static Contests unzipContest(byte[] contest,
            List<Languages> languages, List<Classes> diffClasses)
            throws IOException, JAXBException {
        ZipInputStream in = new ZipInputStream(contest);

        if (in.containsFile("contest.xml") == false) {
            throw new FileNotFoundException("Description file not found in zip archive: contest.xml");
        }

        Contest xmlContest = (Contest) xmlParser.parseString(new String(in.getFile("contest.xml"), "UTF-8"));
        return ZipContest.getContest(in, xmlContest, languages, diffClasses);
    }

    public static Series unzipSerie(byte[] serie,
            List<Languages> languages, List<Classes> diffClasses)
            throws IOException, JAXBException {
        ZipInputStream in = new ZipInputStream(serie);

        if (in.containsFile("serie.xml") == false) {
            throw new FileNotFoundException("Description file not found in zip archive: serie.xml");
        }

        Serie xmlSerie = (Serie) xmlParser.parseString(new String(in.getFile("serie.xml"), "UTF-8"));
        return ZipSerie.getSerie(in, xmlSerie, languages, diffClasses);
    }

    public static Problems unzipProblem(byte[] problem,
            List<Languages> languages, List<Classes> diffClasses)
            throws IOException, JAXBException {
        ZipInputStream in = new ZipInputStream(problem);

        if (in.containsFile("problem.xml") == false) {
            throw new FileNotFoundException("Description file not found in zip archive: problem.xml");
        }

        Problem xmlProblem = (Problem) xmlParser.parseString(new String(in.getFile("problem.xml"), "UTF-8"));
        return ZipProblem.getProblem(in, xmlProblem, languages, diffClasses);
    }

    public static Tests unzipTest(byte[] test,
            List<Languages> languages, List<Classes> diffClasses)
            throws IOException, JAXBException {
        ZipInputStream in = new ZipInputStream(test);

        if (in.containsFile("test.xml") == false) {
            throw new FileNotFoundException("Description file not found in zip archive: test.xml");
        }

        Test xmlTest = (Test) xmlParser.parseString(new String(in.getFile("test.xml"), "UTF-8"));
        return ZipTest.getTest(in, xmlTest);
    }

    public static List<Tests> unzipTests(byte[] tests)
            throws IOException, JAXBException {
        ZipInputStream in = new ZipInputStream(tests);

        if (in.containsFile("problem.xml")) {
            Problem xmlProblem = (Problem) xmlParser.parseString(new String(in.getFile("problem.xml"), "UTF-8"));
            List<Tests> list = new ArrayList<Tests>();
            for (Test xmlTest : xmlProblem.getTests().getTests()) {
                list.add(ZipTest.getTest(in, xmlTest));
            }
            return list;
        } else if (in.containsFile("tests.xml")) {
            pl.umk.mat.zawodyweb.database.xml.Tests xmlTests = (pl.umk.mat.zawodyweb.database.xml.Tests) xmlParser.parseString(new String(in.getFile("tests.xml"), "UTF-8"));
            List<Tests> list = new ArrayList<Tests>();
            for (Test xmlTest : xmlTests.getTests()) {
                list.add(ZipTest.getTest(in, xmlTest));
            }
            return list;
        }
        if (in.containsFile("test.xml")) {
            Test xmlTest = (Test) xmlParser.parseString(new String(in.getFile("test.xml"), "UTF-8"));
            List<Tests> list = new ArrayList<Tests>();
            list.add(ZipTest.getTest(in, xmlTest));
            return list;
        } else {
            throw new FileNotFoundException("Description file not found in zip archive!");
        }

    }
}
