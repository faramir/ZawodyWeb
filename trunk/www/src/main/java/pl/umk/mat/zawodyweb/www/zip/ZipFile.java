/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.umk.mat.zawodyweb.www.zip;

import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.zip.ZipEntry;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.io.output.ByteArrayOutputStream;
import pl.umk.mat.zawodyweb.database.pojo.Contests;
import pl.umk.mat.zawodyweb.database.pojo.Problems;
import pl.umk.mat.zawodyweb.database.pojo.Series;
import pl.umk.mat.zawodyweb.database.pojo.Tests;
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
        } catch (DatatypeConfigurationException e) {
            return null;
        }
    }

    static Date convert(XMLGregorianCalendar xmlDate) {
        return xmlDate.toGregorianCalendar().getTime();
    }

    public static byte[] zipContest(Contests contest) throws IOException, JAXBException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream out = new ZipOutputStream(baos);
        out.setMethod(ZipOutputStream.DEFLATED);
        out.setLevel(java.util.zip.Deflater.BEST_COMPRESSION);

        Object xml = new Contest();

        ZipEntry entry = new ZipEntry("contest.xml");
        out.putNextEntry(entry);
        out.write(xmlParser.toString(xml).getBytes("UTF-8"));
        out.closeEntry();

        out.close();

        return baos.toByteArray();
    }

    public static byte[] zipSerie(Series serie) throws IOException, JAXBException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream out = new ZipOutputStream(baos);
        out.setMethod(ZipOutputStream.DEFLATED);
        out.setLevel(java.util.zip.Deflater.BEST_COMPRESSION);

        Object xml = new Serie();

        ZipEntry entry = new ZipEntry("serie.xml");
        out.putNextEntry(entry);
        out.write(xmlParser.toString(xml).getBytes("UTF-8"));
        out.closeEntry();

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

        ZipEntry entry = new ZipEntry("problem.xml");
        out.putNextEntry(entry);
        out.write(xmlParser.toString(xml).getBytes("UTF-8"));
        out.closeEntry();

        out.close();

        return baos.toByteArray();
    }

    public static byte[] zipTests(List<Tests> tests) throws IOException, JAXBException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream out = new ZipOutputStream(baos);
        out.setMethod(ZipOutputStream.DEFLATED);
        out.setLevel(java.util.zip.Deflater.BEST_COMPRESSION);

        Object xml = new Tests();

        ZipEntry entry = new ZipEntry("tests.xml");
        out.putNextEntry(entry);
        out.write(xmlParser.toString(xml).getBytes("UTF-8"));
        out.closeEntry();

        out.close();

        return baos.toByteArray();
    }

    public static byte[] zipTest(Tests test) throws IOException, JAXBException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream out = new ZipOutputStream(baos);
        out.setMethod(ZipOutputStream.DEFLATED);
        out.setLevel(java.util.zip.Deflater.BEST_COMPRESSION);

        Object xml = new Test();

        ZipEntry entry = new ZipEntry("test.xml");
        out.putNextEntry(entry);
        out.write(xmlParser.toString(xml).getBytes("UTF-8"));
        out.closeEntry();

        out.close();

        return baos.toByteArray();
    }
}
