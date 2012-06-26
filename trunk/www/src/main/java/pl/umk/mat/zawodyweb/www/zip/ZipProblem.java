package pl.umk.mat.zawodyweb.www.zip;

import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
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

    private ZipProblem() {
    }

    static void addProblem(ZipOutputStream out, Problem xmlProblem, Problems problem) throws IOException {
        out.nextProblem();

        xmlProblem.setName(problem.getName());
        xmlProblem.setAbbrev(problem.getAbbrev());
        xmlProblem.setMemlimit(problem.getMemlimit());
        xmlProblem.setCodesize(problem.getCodesize());
        xmlProblem.setDiff(problem.getClasses().getDescription());
        xmlProblem.setVisible(problem.getVisibleinranking());
        xmlProblem.setViewpdf(problem.getViewpdf());

        xmlProblem.setText(setText(out, problem.getText()));
        xmlProblem.setPdf(setPDF(out, problem.getPDF()));

        Problem.Languages languages = new Problem.Languages();
        xmlProblem.setLanguages(languages);

        Problem.Tests tests = new Problem.Tests();
        xmlProblem.setTests(tests);

        List<String> langs = languages.getLanguages();
        for (LanguagesProblems lp : problem.getLanguagesProblemss()) {
            langs.add(lp.getLanguages().getName());
        }

        for (Tests test : problem.getTestss()) {
            Test xmlTest = new Test();
            ZipTest.addTest(out, xmlTest, test);
            xmlProblem.getTests().getTests().add(xmlTest);
        }
    }

    private static String setText(ZipOutputStream out, String text) throws IOException {
        String textFile = String.format("problem%03d.html", out.getProblem());
        ZipEntry entry = new ZipEntry(textFile);
        out.putNextEntry(entry);
        out.write(text.getBytes("UTF-8"));
        out.closeEntry();

        return textFile;
    }

    private static String setPDF(ZipOutputStream out, PDF pdf) throws IOException {
        if (pdf == null) {
            return null;
        }

        String pdfFile = String.format("problem%03d.pdf", out.getProblem());
        ZipEntry entry = new ZipEntry(pdfFile);
        out.putNextEntry(entry);
        out.write(pdf.getPdf());
        out.closeEntry();

        return pdfFile;
    }
}
