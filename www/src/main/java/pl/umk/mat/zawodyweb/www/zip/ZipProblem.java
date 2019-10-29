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
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.zip.ZipEntry;

import pl.umk.mat.zawodyweb.database.pojo.*;
import pl.umk.mat.zawodyweb.database.xml.FilesType;
import pl.umk.mat.zawodyweb.database.xml.ProblemType;
import pl.umk.mat.zawodyweb.database.xml.TestType;

/**
 * @author faramir
 */
public class ZipProblem {

    private ZipProblem() {
    }

    static void addProblem(ZipOutputStream out, ProblemType xmlProblem, Problems problem) throws IOException {
        out.nextProblem();

        xmlProblem.setName(problem.getName());
        xmlProblem.setAbbrev(problem.getAbbrev());
        xmlProblem.setMemlimit(problem.getMemlimit());
        xmlProblem.setCodesize(problem.getCodesize());
        xmlProblem.setDiff(problem.getClasses().getDescription());
        xmlProblem.setVisible(problem.getVisibleinranking());
        xmlProblem.setViewpdf(problem.getViewpdf());
        xmlProblem.setConfig(problem.getConfig());

        xmlProblem.setText(setText(out, problem.getText()));
        xmlProblem.setFiles(setFiles(out, problem.getFiles()));

        ProblemType.Languages languages = new ProblemType.Languages();
        xmlProblem.setLanguages(languages);

        ProblemType.Tests tests = new ProblemType.Tests();
        xmlProblem.setTests(tests);

        List<String> langs = languages.getLanguage();
        for (LanguagesProblems lp : problem.getLanguagesProblemss()) {
            langs.add(lp.getLanguages().getName());
        }

        for (Tests test : problem.getTestss()) {
            TestType xmlTest = new TestType();
            ZipTest.addTest(out, xmlTest, test);
            xmlProblem.getTests().getTest().add(xmlTest);
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

    private static FilesType setFiles(ZipOutputStream out, Files files) throws IOException {
        if (files == null) {
            return null;
        }

        String pdfFile = String.format("problem%03d.files", out.getProblem());
        ZipEntry entry = new ZipEntry(pdfFile);
        out.putNextEntry(entry);
        out.write(files.getBytes());
        out.closeEntry();

        FilesType filesType = new FilesType();
        filesType.setBytes(pdfFile);
        filesType.setFilename(files.getFilename());
        filesType.setExtension(files.getExtension());

        return filesType;
    }

    static Problems getProblem(ZipInputStream in, ProblemType xmlProblem,
                               List<Languages> languages, List<Classes> diffClasses)
            throws UnsupportedEncodingException, FileNotFoundException {
        Problems problem = new Problems();
        problem.setName(xmlProblem.getName());
        problem.setAbbrev(xmlProblem.getAbbrev());
        problem.setMemlimit(xmlProblem.getMemlimit());
        problem.setCodesize(xmlProblem.getCodesize());
        problem.setVisibleinranking(xmlProblem.isVisible());
        problem.setViewpdf(xmlProblem.isViewpdf());
        problem.setConfig(xmlProblem.getConfig());

        String textFile = xmlProblem.getText();
        if (textFile == null || textFile.isEmpty()) {
            throw new FileNotFoundException("Text file not found in zip archive!");
        }
        if (in.containsFile(textFile) == false) {
            throw new FileNotFoundException("Text file not found in zip archive: " + textFile);
        }
        problem.setText(new String(in.getFile(textFile), "UTF-8"));

        FilesType filesType = xmlProblem.getFiles();

        if (filesType != null) {
            String bytesLocation = filesType.getBytes();
            if (bytesLocation != null && bytesLocation.isEmpty() == false) {
                if (in.containsFile(bytesLocation) == false) {
                    throw new FileNotFoundException("Files file not found in zip archive: " + bytesLocation);
                } else {
                    Files files = new Files();
                    files.setFilename(filesType.getFilename());
                    files.setExtension(filesType.getExtension());
                    files.setBytes(in.getFile(bytesLocation));
                    problem.setFiles(files);
                }
            }
        }

        String diff = xmlProblem.getDiff();
        if (diffClasses != null && diff != null) {
            for (Classes c : diffClasses) {
                if (diff.equals(c.getDescription())) {
                    problem.setClasses(c);
                    break;
                }
            }
        }

        List<String> langs = xmlProblem.getLanguages().getLanguage();
        if (languages != null && langs != null) {
            List<LanguagesProblems> lps = new ArrayList<>();

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

            problem.setLanguagesProblemss(lps);
        }

        if (xmlProblem.getTests().getTest() != null) {
            SortedSet<Tests> tests = new TreeSet<>();

            for (TestType test : xmlProblem.getTests().getTest()) {
                tests.add(ZipTest.getTest(in, test));
            }

            problem.setTestss(tests);
        }

        return problem;
    }
}
