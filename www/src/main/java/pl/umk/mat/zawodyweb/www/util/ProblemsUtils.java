/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.www.util;

import java.util.List;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.LanguagesProblemsDAO;
import pl.umk.mat.zawodyweb.database.TestsDAO;
import pl.umk.mat.zawodyweb.database.pojo.LanguagesProblems;
import pl.umk.mat.zawodyweb.database.pojo.Files;
import pl.umk.mat.zawodyweb.database.pojo.Problems;
import pl.umk.mat.zawodyweb.database.pojo.Series;
import pl.umk.mat.zawodyweb.database.pojo.Submits;
import pl.umk.mat.zawodyweb.database.pojo.Tests;

/**
 *
 * @author Jakub Prabucki
 */
public class ProblemsUtils {

    static private final ProblemsUtils instance = new ProblemsUtils();

    private ProblemsUtils() {
    }

    public static ProblemsUtils getInstance() {
        return instance;
    }

    public Problems copyProblem(Problems sourceProblem, Series destinationSerie, String abbrev, String name/*, boolean copyUsersSolutions*/) {
        Problems destinationProblem = new Problems();

        if (abbrev == null || abbrev.isEmpty()) {
            destinationProblem.setAbbrev(sourceProblem.getAbbrev());
        } else {
            destinationProblem.setAbbrev(abbrev);
        }

        if (name == null || name.isEmpty()) {
            destinationProblem.setName(sourceProblem.getName());
        } else {
            destinationProblem.setName(name);
        }

        destinationProblem.setClasses(sourceProblem.getClasses());
        destinationProblem.setMemlimit(sourceProblem.getMemlimit());
        destinationProblem.setCodesize(sourceProblem.getCodesize());
        destinationProblem.setVisibleinranking(sourceProblem.getVisibleinranking());
        destinationProblem.setViewpdf(sourceProblem.getViewpdf());
        destinationProblem.setConfig(sourceProblem.getConfig());

        Files files = sourceProblem.getFiles();
        if (files != null) {
            Files newFiles = new Files();
            newFiles.setPdf(files.getPdf());
            DAOFactory.DEFAULT.buildPDFDAO().saveOrUpdate(newFiles);
            destinationProblem.setFiles(newFiles);
        }

        destinationProblem.setSeries(destinationSerie);
        destinationProblem.setText(sourceProblem.getText());
        DAOFactory.DEFAULT.buildProblemsDAO().save(destinationProblem);

        LanguagesProblemsDAO languagesProblemsDAO = DAOFactory.DEFAULT.buildLanguagesProblemsDAO();
        for (LanguagesProblems oldLanguage : sourceProblem.getLanguagesProblemss()) {
            LanguagesProblems newLanguage = new LanguagesProblems();
            newLanguage.setLanguages(oldLanguage.getLanguages());
            newLanguage.setProblems(destinationProblem);
            languagesProblemsDAO.save(newLanguage);
        }

        TestsDAO testsDAO = DAOFactory.DEFAULT.buildTestsDAO();
        for (Tests oldTest : sourceProblem.getTestss()) {
            Tests newTest = new Tests();
            newTest.setInput(oldTest.getInput());
            newTest.setMaxpoints(oldTest.getMaxpoints());
            newTest.setOutput(oldTest.getOutput());
            newTest.setProblems(destinationProblem);
            newTest.setTestorder(oldTest.getTestorder());
            newTest.setTimelimit(oldTest.getTimelimit());
            newTest.setVisibility(oldTest.getVisibility());
            newTest.setConfig(oldTest.getConfig());
            testsDAO.save(newTest);
        }

        return destinationProblem;
    }

    public void reJudge(Problems problem) {
        List<Submits> findByProblemsid = DAOFactory.DEFAULT.buildSubmitsDAO().findByProblemsid(problem.getId());
        for (Submits submit : findByProblemsid) {
            SubmitsUtils.getInstance().reJudge(submit);
        }
    }
}
