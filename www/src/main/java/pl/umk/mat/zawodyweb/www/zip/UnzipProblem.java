package pl.umk.mat.zawodyweb.www.zip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.xml.bind.JAXBException;
import pl.umk.mat.zawodyweb.database.pojo.Classes;
import pl.umk.mat.zawodyweb.database.pojo.Languages;
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
public class UnzipProblem {
    
    private static final int BUFFER = 2048;
    private static final XmlParser xmlParser = new XmlParser();
    private Map<String, byte[]> entries;
    private Problems problem;
    private List<Languages> languages;
    private List<Classes> diffClasses;
    
    public UnzipProblem() {
        this(null, null);
    }
    
    public UnzipProblem(List<Languages> languages, List<Classes> diffClasses) {
        this.languages = languages;
        this.diffClasses = diffClasses;
    }
    
    private void fillEntries(byte[] zipfile) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(zipfile);
        ZipInputStream in = new ZipInputStream(bais);
        
        entries = new HashMap<String, byte[]>();
        
        ZipEntry entry;
        
        while ((entry = in.getNextEntry()) != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] data = new byte[BUFFER];
            int count;
            while ((count = in.read(data, 0, BUFFER)) != -1) {
                baos.write(data, 0, count);
            }
            baos.flush();
            baos.close();
            
            entries.put(entry.getName(), baos.toByteArray());
        }
    }
    
    public Problems getProblem(byte[] zipfile) throws IOException, JAXBException {
        fillEntries(zipfile);
        
        if (entries.containsKey("problem.xml") == false) {
            throw new FileNotFoundException("Problem description file not found in zip archive: problem.xml");
        }
        
        Problem xmlProblem = (Problem) xmlParser.parseString(new String(entries.get("problem.xml"), "UTF-8"));
        
        problem = new Problems();
        problem.setName(xmlProblem.getName());
        problem.setAbbrev(xmlProblem.getAbbrev());
        problem.setMemlimit(xmlProblem.getMemlimit());
        problem.setCodesize(xmlProblem.getCodesize());
        problem.setVisibleinranking(xmlProblem.getVisible());
        problem.setViewpdf(xmlProblem.getViewpdf());
        
        problem.setText(getText(xmlProblem.getText()));
        
        problem.setPDF(getPdf(xmlProblem.getPdf()));
        
        problem.setClasses(getDiff(xmlProblem.getDiff()));
        
        problem.setLanguagesProblemss(getLanguageProblem(xmlProblem.getLanguages().getLanguages()));
        
        problem.setTestss(getTests(xmlProblem.getTests().getTests()));
        
        return problem;
    }
    
    private List<Tests> getTests(List<Test> xmlTest) throws IOException {
        List<Tests> tests = new ArrayList<Tests>();
        
        for (Test test : xmlTest) {
            Tests t = getTest(test);
            if (t != null) {
                tests.add(t);
            }
        }
        
        return tests;
    }
    
    public static void main(String[] args) throws Throwable {
        new UnzipProblem().getTests(new byte[0]);
    }
    
    public List<Tests> getTests(byte[] zipfile) throws IOException, JAXBException {
        fillEntries(zipfile);
        
        List<Test> tests = null;
        
        if (entries.containsKey("problem.xml") == true) {
            Problem xmlProblem = (Problem) xmlParser.parseString(new String(entries.get("problem.xml"), "UTF-8"));
            tests = xmlProblem.getTests().getTests();
        } else if (entries.containsKey("tests.xml") == true) {
            pl.umk.mat.zawodyweb.database.xml.Tests xmlTests =
                    (pl.umk.mat.zawodyweb.database.xml.Tests) xmlParser.parseString(new String(entries.get("tests.xml"), "UTF-8"));
            tests = xmlTests.getTests();
        } else if (entries.containsKey("test.xml") == true) {
            Test xmlTest = (Test) xmlParser.parseString(new String(entries.get("test.xml"), "UTF-8"));
            tests = Arrays.asList(new Test[]{xmlTest});
        } else {
            throw new FileNotFoundException("Description file (problem.xml, tests.xml, test.xml) not found in zip archive");
        }
        
        if (tests == null) {
            throw new NullPointerException("Tests desciption not found");
        }
        
        return getTests(tests);
    }
    
    private Classes getDiff(String diff) {
        if (diffClasses == null || diff == null) {
            return null;
        }
        for (Classes c : diffClasses) {
            if (diff.equals(c.getDescription())) {
                return c;
            }
        }
        return null;
    }
    
    private List<LanguagesProblems> getLanguageProblem(List<String> langs) {
        if (languages == null || langs == null) {
            return null;
        }
        List<LanguagesProblems> lps = new ArrayList<LanguagesProblems>();
        
        nextLang:
        for (String lang : langs) {
            for (Languages language : languages) {
                if (lang.equals(language.getName())) {
                    LanguagesProblems lp = new LanguagesProblems();
                    lp.setLanguages(language);
                    lps.add(lp);
                    continue nextLang;
                }
            }
        }
        
        return lps;
    }
    
    private Tests getTest(Test test) throws IOException {
        String infile = test.getInput();
        String outfile = test.getOutput();
        
        if (infile == null || infile.isEmpty()) {
            return null;
        }
        
        if (outfile == null || outfile.isEmpty()) {
            return null;
        }
        
        if (entries.containsKey(infile) == false) {
            throw new FileNotFoundException("Input file not found in zip archive: " + infile);
        }
        
        if (entries.containsKey(outfile) == false) {
            throw new FileNotFoundException("Output file not found in zip archive: " + outfile);
        }
        
        Integer maxpoints = test.getMaxpoints();
        Integer testorder = test.getOrder();
        Integer timelimit = test.getTimelimit();
        
        Tests tests = new Tests();
        tests.setInput(new String(entries.get(infile), "UTF-8"));
        tests.setOutput(new String(entries.get(outfile), "UTF-8"));
        tests.setMaxpoints(maxpoints);
        tests.setTestorder(testorder);
        tests.setTimelimit(timelimit);
        tests.setVisibility(1); // FIXME: należy wartość pobrać z pliku XML (brak)

        return tests;
    }
    
    private String getText(String filename) throws IOException {
        if (filename == null || filename.isEmpty()) {
            return null;
        }
        
        if (entries.containsKey(filename) == false) {
            throw new FileNotFoundException("Text file not found in zip archive: " + filename);
        }
        
        return new String(entries.get(filename), "UTF-8");
    }
    
    private PDF getPdf(String filename) throws IOException {
        if (filename == null || filename.isEmpty()) {
            return null;
        }
        
        if (entries.containsKey(filename) == false) {
            throw new FileNotFoundException("PDF file not found in zip archive: " + filename);
        }
        
        PDF pdf = new PDF();
        
        pdf.setPdf(entries.get(filename));
        
        return pdf;
    }
    
    public Problems getProblem() {
        return problem;
    }
}
