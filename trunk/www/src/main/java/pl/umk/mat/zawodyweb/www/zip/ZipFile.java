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
public class ZipFile {

    private static final XmlParser xmlParser = new XmlParser();
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private byte[] data;
    private ZipOutputStream out;
    private int testsCount;
    private Problem xmlProblem;

    public ZipFile() {
        out = new ZipOutputStream(baos);
        out.setMethod(ZipOutputStream.DEFLATED);
        out.setLevel(7);
    }

    public void setProblem(Problems problem) throws IOException {
        xmlProblem = new Problem();

        xmlProblem.setName(problem.getName());
        xmlProblem.setAbbrev(problem.getAbbrev());
        xmlProblem.setMemlimit(problem.getMemlimit());
        xmlProblem.setCodesize(problem.getCodesize());
        xmlProblem.setDiff(problem.getClasses().getDescription());
        xmlProblem.setVisible(problem.getVisibleinranking());

        setText(problem.getText());
        setPDF(problem.getPDF());

        Problem.Languages languages = new Problem.Languages();
        xmlProblem.setLanguages(languages);

        Problem.Tests tests = new Problem.Tests();
        xmlProblem.setTests(tests);

        List<String> langs = languages.getLanguage();
        for (LanguagesProblems lp : problem.getLanguagesProblemss()) {
            langs.add(lp.getLanguages().getName());
        }

        for (Tests test : problem.getTestss()) {
            addTest(test);
        }
    }

    private void addTest(Tests test) throws IOException {
        ZipEntry entry;

        String inputFile = String.format("in%02d.txt", testsCount);
        entry = new ZipEntry(inputFile);
        out.putNextEntry(entry);
        out.write(test.getInput().getBytes());
        out.closeEntry();

        String outputFile = String.format("out%02d.txt", testsCount);
        entry = new ZipEntry(outputFile);
        out.putNextEntry(entry);
        out.write(test.getOutput().getBytes());
        out.closeEntry();

        Test xmlTest = new Test();
        xmlTest.setInput(inputFile);
        xmlTest.setOutput(outputFile);
        xmlTest.setMaxpoints(test.getMaxpoints());
        xmlTest.setOrder(test.getTestorder());
        xmlTest.setTimelimit(test.getTimelimit());

        ++testsCount;
        xmlProblem.getTests().getTest().add(xmlTest);
    }

    private void setText(String text) throws IOException {
        String textFile = "problem.html";
        ZipEntry entry = new ZipEntry(textFile);
        out.putNextEntry(entry);
        out.write(text.getBytes());
        out.closeEntry();

        xmlProblem.setText(textFile);
    }

    private void setPDF(PDF pdf) throws IOException {
        if (pdf == null) {
            return;
        }

        String pdfFile = "problem.pdf";
        ZipEntry entry = new ZipEntry(pdfFile);
        out.putNextEntry(entry);
        out.write(pdf.getPdf());
        out.closeEntry();

        xmlProblem.setPdf(pdfFile);
    }

    public byte[] finish() throws IOException, JAXBException {
        ZipEntry entry = new ZipEntry("problem.xml");
        out.putNextEntry(entry);
        out.write(xmlParser.toString(xmlProblem).getBytes());
        out.closeEntry();

        out.close();

        data = baos.toByteArray();
        baos = null;

        return data;
    }

    public byte[] getData() {
        return data;
    }
}
