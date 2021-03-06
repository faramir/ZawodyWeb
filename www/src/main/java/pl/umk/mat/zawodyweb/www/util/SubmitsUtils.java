/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.www.util;

import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.ResultsDAO;
import pl.umk.mat.zawodyweb.database.SubmitsStateEnum;
import pl.umk.mat.zawodyweb.database.hibernate.HibernateUtil;
import pl.umk.mat.zawodyweb.database.pojo.Results;
import pl.umk.mat.zawodyweb.database.pojo.Submits;
import pl.umk.mat.zawodyweb.www.JudgeManagerConnector;

/**
 *
 * @author Jakub Prabucki
 */
public class SubmitsUtils {

    static private final SubmitsUtils instance = new SubmitsUtils();

    private SubmitsUtils() {
    }

    public static SubmitsUtils getInstance() {
        return instance;
    }

    public void reJudge(Submits submit) {
        submit.setState(SubmitsStateEnum.WAIT.getCode());
        DAOFactory.DEFAULT.buildSubmitsDAO().saveOrUpdate(submit);
        ResultsDAO resultsDAO = DAOFactory.DEFAULT.buildResultsDAO();
        for (Results r : resultsDAO.findBySubmitsid(submit.getId())) {
            resultsDAO.delete(r);
        }

        JudgeManagerConnector.getInstance().sentToJudgeManager(submit.getId());
    }
}
