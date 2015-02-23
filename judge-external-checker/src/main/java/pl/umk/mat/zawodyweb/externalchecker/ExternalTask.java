/*
 * Copyright (c) 2015, Marek Nowicki
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.externalchecker;

import pl.umk.mat.zawodyweb.judge.commons.TestOutput;
import pl.umk.mat.zawodyweb.judge.commons.TestInput;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.hibernate.Transaction;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.ResultsDAO;
import pl.umk.mat.zawodyweb.database.ResultsStatusEnum;
import pl.umk.mat.zawodyweb.database.SubmitsDAO;
import pl.umk.mat.zawodyweb.database.SubmitsStateEnum;
import pl.umk.mat.zawodyweb.database.hibernate.HibernateUtil;
import pl.umk.mat.zawodyweb.database.pojo.Results;
import pl.umk.mat.zawodyweb.database.pojo.Submits;
import pl.umk.mat.zawodyweb.database.pojo.Tests;

/**
 *
 * @author faramir
 */
public class ExternalTask implements Runnable {

    private static final Logger logger = Logger.getLogger(ExternalTask.class);

    private final int submitId;
    private final ExternalInterface external;

    public ExternalTask(int submitId, ExternalInterface external) {
        this.submitId = submitId;
        this.external = external;
    }

    @Override
    public void run() {
        Transaction transaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        try {
            SubmitsDAO submitsDAO = DAOFactory.DEFAULT.buildSubmitsDAO();
            ResultsDAO resultsDAO = DAOFactory.DEFAULT.buildResultsDAO();

            boolean manualResult = false;
            boolean externalResult = false;

            Submits submit = submitsDAO.getById(submitId);

            for (Results result : submit.getResultss()) {
                if (result.getStatus() == ResultsStatusEnum.MANUAL.getCode()) {
                    manualResult = true;
                    continue;
                } else if (result.getStatus() != ResultsStatusEnum.EXTERNAL.getCode()) {
                    continue;
                }

                Tests test = result.getTests();
                Properties properties = new Properties(submit.getProblems().loadProperties());
                properties.putAll(test.loadProperties());

                TestInput testInput = new TestInput(test.getInput(),
                        test.getMaxpoints(),
                        test.getTimelimit(),
                        submit.getProblems().getMemlimit(),
                        properties
                );
                TestOutput testOutput = new TestOutput(result.getTests().getOutput());

                String externalId = result.getNotes().substring(external.getPrefix().length() + 1);

                TestOutput output = external.check(externalId, testInput, testOutput/* TODO: load CheckerInterface?*/);

                if (output == null || output.getStatus() == ResultsStatusEnum.EXTERNAL.getCode()) {
                    externalResult = true;
                    continue;
                }

                result.setStatus(output.getStatus());
                result.setNotes(output.getNotes());
                result.setPoints(output.getPoints());
                result.setRuntime(output.getRuntime());
                result.setMemory(output.getMemUsed());

                resultsDAO.saveOrUpdate(result);
            }

            if (externalResult == true) {
                submit.setState(SubmitsStateEnum.EXTERNAL.getCode());
            } else if (manualResult == true) {
                submit.setState(SubmitsStateEnum.MANUAL.getCode());
            } else {
                submit.setState(SubmitsStateEnum.DONE.getCode());
            }

            submitsDAO.saveOrUpdate(submit);

            transaction.commit();
        } catch (Throwable t) {
            logger.fatal(t);
            transaction.rollback();
        }
    }
}
