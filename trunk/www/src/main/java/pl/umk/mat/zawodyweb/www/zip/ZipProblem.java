package pl.umk.mat.zawodyweb.www.zip;

import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.xml.bind.JAXBException;
import org.apache.commons.io.output.ByteArrayOutputStream;
import pl.umk.mat.zawodyweb.database.pojo.LanguagesProblems;
import pl.umk.mat.zawodyweb.database.pojo.PDF;
import pl.umk.mat.zawodyweb.database.pojo.Problems;
import pl.umk.mat.zawodyweb.database.pojo.Tests;
import pl.umk.mat.zawodyweb.database.xml.Problem;
import pl.umk.mat.zawodyweb.database.xml.Test;

/**
 *
 * @author faramir
 */
public class ZipProblem {

    private static final XmlParser xmlParser = new XmlParser();
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private byte[] data;
    private ZipOutputStream out;
    private int testsCount;
    private Problem xmlProblem;
    private Test xmlTest;

    public ZipProblem() {
        out = new ZipOutputStream(baos);
        out.setMethod(ZipOutputStream.DEFLATED);
        out.setLevel(java.util.zip.Deflater.BEST_COMPRESSION);
    }

    public void setTest(Tests test) throws IOException {
        if (test.getTestorder() != null) {
            testsCount = test.getTestorder();
        } else {
            testsCount = 0;
        }
        xmlProblem = null;
        xmlTest = addTest(test);
    }

    public void setProblem(Problems problem) throws IOException {
        testsCount = 0;
        xmlTest = null;
        xmlProblem = new Problem();

        xmlProblem.setName(problem.getName());
        xmlProblem.setAbbrev(problem.getAbbrev());
        xmlProblem.setMemlimit(problem.getMemlimit());
        xmlProblem.setCodesize(problem.getCodesize());
        xmlProblem.setDiff(problem.getClasses().getDescription());
        xmlProblem.setVisible(problem.getVisibleinranking());
        xmlProblem.setViewpdf(problem.getViewpdf());

        xmlProblem.setText(setText(problem.getText()));
        xmlProblem.setPdf(setPDF(problem.getPDF()));

        Problem.Languages languages = new Problem.Languages();
        xmlProblem.setLanguages(languages);

        Problem.Tests tests = new Problem.Tests();
        xmlProblem.setTests(tests);

        List<String> langs = languages.getLanguages();
        for (LanguagesProblems lp : problem.getLanguagesProblemss()) {
            langs.add(lp.getLanguages().getName());
        }

        for (Tests test : problem.getTestss()) {
            xmlProblem.getTests().getTests().add(addTest(test));
            ++testsCount;
        }
    }

    private Test addTest(Tests test) throws IOException {
        ZipEntry entry;

        String inputFile = String.format("in%02d.txt", testsCount);
        entry = new ZipEntry(inputFile);
        out.putNextEntry(entry);
        out.write(test.getInput().getBytes("UTF-8"));
        out.closeEntry();

        String outputFile = String.format("out%02d.txt", testsCount);
        entry = new ZipEntry(outputFile);
        out.putNextEntry(entry);
        out.write(test.getOutput().getBytes("UTF-8"));
        out.closeEntry();

        Test xTest = new Test();
        xTest.setInput(inputFile);
        xTest.setOutput(outputFile);
        xTest.setMaxpoints(test.getMaxpoints());
        xTest.setOrder(test.getTestorder());
        xTest.setTimelimit(test.getTimelimit());

        return xTest;
    }

    private String setText(String text) throws IOException {
        String textFile = "problem.html";
        ZipEntry entry = new ZipEntry(textFile);
        out.putNextEntry(entry);
        out.write(text.getBytes("UTF-8"));
        out.closeEntry();

        return textFile;
    }

    private String setPDF(PDF pdf) throws IOException {
        if (pdf == null) {
            return null;
        }

        String pdfFile = "problem.pdf";
        ZipEntry entry = new ZipEntry(pdfFile);
        out.putNextEntry(entry);
        out.write(pdf.getPdf());
        out.closeEntry();

        return pdfFile;
    }

    public byte[] finish() throws IOException, JAXBException {
        if (xmlProblem != null) {
            ZipEntry entry = new ZipEntry("problem.xml");
            out.putNextEntry(entry);
            out.write(xmlParser.toString(xmlProblem).getBytes("UTF-8"));
            out.closeEntry();
        } else if (xmlTest != null) {
            ZipEntry entry = new ZipEntry("test.xml");
            out.putNextEntry(entry);
            out.write(xmlParser.toString(xmlTest).getBytes("UTF-8"));
            out.closeEntry();
        }

        out.close();

        data = baos.toByteArray();
        baos = null;

        return data;
    }

    public byte[] getData() {
        return data;
    }
}
