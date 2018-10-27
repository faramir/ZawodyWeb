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
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.ResultsStatusEnum;
import pl.umk.mat.zawodyweb.database.SubmitsDAO;
import pl.umk.mat.zawodyweb.database.SubmitsStateEnum;
import pl.umk.mat.zawodyweb.database.hibernate.HibernateUtil;
import pl.umk.mat.zawodyweb.database.pojo.Results;
import pl.umk.mat.zawodyweb.database.pojo.Submits;
import pl.umk.mat.zawodyweb.database.pojo.Tests;

/**
 * @author Marek Nowicki /faramir/
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

            boolean manualResult = false;
            boolean externalResult = false;
            boolean changedResult = false;

            Submits submit = submitsDAO.getById(submitId);

            for (Results result : submit.getResultss()) {
                if (result.getStatus() == ResultsStatusEnum.MANUAL.getCode()) {
                    manualResult = true;
                    continue;
                } else if (result.getStatus() != ResultsStatusEnum.EXTERNAL.getCode()) {
                    continue;
                }

                Tests test = result.getTests();
                TestInput testInput = new TestInput(test.getInput(),
                        test.getMaxpoints(),
                        test.getTimelimit(),
                        submit.getProblems().getMemlimit(),
                        test.loadProperties()
                );
                TestOutput testOutput = new TestOutput(result.getTests().getOutput());

                String notes = result.getNotes();
                if (notes == null) {
                    notes = "";
                }

                String externalId = notes.substring(external.getPrefix().length() + 1);

                TestOutput output = external.check(externalId, testInput, testOutput/* TODO: load CheckerInterface?*/);

                if (output == null || output.getStatus() == ResultsStatusEnum.EXTERNAL.getCode()) {
                    externalResult = true;
                    continue;
                }

                result.setStatus(output.getStatus());
                result.setPoints(output.getPoints());
                result.setRuntime(output.getRuntime());
                result.setMemory(output.getMemUsed());
                result.setNotes(output.getNotes());

                logger.debug("Saving result:"
                        + " status: " + ResultsStatusEnum.getByCode(result.getStatus())
                        + ", points: " + result.getPoints()
                        + ", runtime: " + result.getRuntime()
                        + ", memory: " + result.getMemory()
                        + ", notes: '" + result.getNotes() + "'");
                DAOFactory.DEFAULT.buildResultsDAO().update(result);
                changedResult = true;
            }

            if (changedResult) {
                if (externalResult == true) {
                    submit.setState(SubmitsStateEnum.EXTERNAL.getCode());
                } else if (manualResult == true) {
                    submit.setState(SubmitsStateEnum.MANUAL.getCode());
                } else {
                    submit.setState(SubmitsStateEnum.DONE.getCode());
                }

                logger.debug("Changing submit status to: " + SubmitsStateEnum.getByCode(submit.getState()));
                submitsDAO.update(submit);

                logger.debug("Commiting changes");
                transaction.commit();
            } else {
                logger.debug("Nothing changed.");
                transaction.rollback();
            }
        } catch (Throwable t) {
            logger.fatal("Exception in ExternalTask", t);
            transaction.rollback();
        }
    }
}
