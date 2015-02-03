/*
 * Copyright (c) 2015, Marek Nowicki
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.externalchecker;

import org.hibernate.Transaction;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.ResultsDAO;
import pl.umk.mat.zawodyweb.database.ResultsStatusEnum;
import pl.umk.mat.zawodyweb.database.SubmitsDAO;
import pl.umk.mat.zawodyweb.database.SubmitsStateEnum;
import pl.umk.mat.zawodyweb.database.hibernate.HibernateUtil;
import pl.umk.mat.zawodyweb.database.pojo.Results;
import pl.umk.mat.zawodyweb.database.pojo.Submits;

/**
 *
 * @author faramir
 */
public class ExternalRunner implements Runnable {

    private final Submits submit;
    private final ExternalInterface external;

    public ExternalRunner(Submits submit, ExternalInterface external) {
        this.submit = submit;
        this.external = external;
    }

    @Override
    public void run() {
        Transaction transaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        try {
            SubmitsDAO submitsDAO = DAOFactory.DEFAULT.buildSubmitsDAO();
            ResultsDAO resultsDAO = DAOFactory.DEFAULT.buildResultsDAO();
            
            Submits outputSubmit = external.check(submit);
            if (outputSubmit != null) {
                outputSubmit.setState(SubmitsStateEnum.DONE.getCode());

                boolean manualResult = false;
                boolean externalResult = false;

                for (Results result : outputSubmit.getResultss()) {
                    if (result.getStatus() == ResultsStatusEnum.EXTERNAL.getCode()) {
                        externalResult = true;
                    } else if (result.getStatus() == ResultsStatusEnum.MANUAL.getCode()) {
                        manualResult = true;
                    }
                    
                    resultsDAO.saveOrUpdate(result);
                }


                if (externalResult == true) {
                    outputSubmit.setState(SubmitsStateEnum.EXTERNAL.getCode());
                } else if (manualResult == true) {
                    outputSubmit.setState(SubmitsStateEnum.MANUAL.getCode());
                }

                submitsDAO.saveOrUpdate(outputSubmit);

                transaction.commit();
            }
        } catch (Throwable t) {
            t.printStackTrace(System.err);
            transaction.rollback();
        }
    }
}