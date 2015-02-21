/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.database.hibernate;

import pl.umk.mat.zawodyweb.database.AliasesDAO;
import pl.umk.mat.zawodyweb.database.ClassesDAO;
import pl.umk.mat.zawodyweb.database.ContestsDAO;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.LanguagesDAO;
import pl.umk.mat.zawodyweb.database.LanguagesProblemsDAO;
import pl.umk.mat.zawodyweb.database.PDFDAO;
import pl.umk.mat.zawodyweb.database.ProblemsDAO;
import pl.umk.mat.zawodyweb.database.QuestionsDAO;
import pl.umk.mat.zawodyweb.database.ResultsDAO;
import pl.umk.mat.zawodyweb.database.RolesDAO;
import pl.umk.mat.zawodyweb.database.SeriesDAO;
import pl.umk.mat.zawodyweb.database.SubmitsDAO;
import pl.umk.mat.zawodyweb.database.TestsDAO;
import pl.umk.mat.zawodyweb.database.UserLogDAO;
import pl.umk.mat.zawodyweb.database.UsersDAO;
import pl.umk.mat.zawodyweb.database.UsersRolesDAO;

/**
 * Generated at Fri May 08 19:01:00 CEST 2009
 *
 * @see http://www.hibernate.org/43.html
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 */
public class HibernateDAOFactory extends DAOFactory {

    /* (non-Javadoc)
     * @see pl.umk.mat.zawodyweb.database.DAOFactory#buildSeriesDAO()
     */
    @Override
    public SeriesDAO buildSeriesDAO() {
        return new SeriesHibernateDAO();
    }

    /* (non-Javadoc)
     * @see pl.umk.mat.zawodyweb.database.DAOFactory#buildUsersDAO()
     */
    @Override
    public UserLogDAO buildUserLogDAO() {
        return new UserLogHibernateDAO();
    }

    /* (non-Javadoc)
     * @see pl.umk.mat.zawodyweb.database.DAOFactory#buildUsersDAO()
     */
    @Override
    public UsersDAO buildUsersDAO() {
        return new UsersHibernateDAO();
    }

    /* (non-Javadoc)
     * @see pl.umk.mat.zawodyweb.database.DAOFactory#buildLanguagesDAO()
     */
    @Override
    public LanguagesDAO buildLanguagesDAO() {
        return new LanguagesHibernateDAO();
    }

    /* (non-Javadoc)
     * @see pl.umk.mat.zawodyweb.database.DAOFactory#buildTestsDAO()
     */
    @Override
    public TestsDAO buildTestsDAO() {
        return new TestsHibernateDAO();
    }

    /* (non-Javadoc)
     * @see pl.umk.mat.zawodyweb.database.DAOFactory#buildResultsDAO()
     */
    @Override
    public ResultsDAO buildResultsDAO() {
        return new ResultsHibernateDAO();
    }

    /* (non-Javadoc)
     * @see pl.umk.mat.zawodyweb.database.DAOFactory#buildClassesDAO()
     */
    @Override
    public AliasesDAO buildAliasesDAO() {
        return new AliasesHibernateDAO();
    }

    /* (non-Javadoc)
     * @see pl.umk.mat.zawodyweb.database.DAOFactory#buildClassesDAO()
     */
    @Override
    public ClassesDAO buildClassesDAO() {
        return new ClassesHibernateDAO();
    }

    /* (non-Javadoc)
     * @see pl.umk.mat.zawodyweb.database.DAOFactory#buildSubmitsDAO()
     */
    @Override
    public SubmitsDAO buildSubmitsDAO() {
        return new SubmitsHibernateDAO();
    }

    /* (non-Javadoc)
     * @see pl.umk.mat.zawodyweb.database.DAOFactory#buildContestsDAO()
     */
    @Override
    public ContestsDAO buildContestsDAO() {
        return new ContestsHibernateDAO();
    }

    /* (non-Javadoc)
     * @see pl.umk.mat.zawodyweb.database.DAOFactory#buildProblemsDAO()
     */
    @Override
    public ProblemsDAO buildProblemsDAO() {
        return new ProblemsHibernateDAO();
    }

    /* (non-Javadoc)
     * @see pl.umk.mat.zawodyweb.database.DAOFactory#buildQuestionsDAO()
     */
    @Override
    public QuestionsDAO buildQuestionsDAO() {
        return new QuestionsHibernateDAO();
    }

    /* (non-Javadoc)
     * @see pl.umk.mat.zawodyweb.database.DAOFactory#buildUsersRolesDAO()
     */
    @Override
    public UsersRolesDAO buildUsersRolesDAO() {
        return new UsersRolesHibernateDAO();
    }

    /* (non-Javadoc)
     * @see pl.umk.mat.zawodyweb.database.DAOFactory#buildRolesDAO()
     */
    @Override
    public RolesDAO buildRolesDAO() {
        return new RolesHibernateDAO();
    }

    /* (non-Javadoc)
     * @see pl.umk.mat.zawodyweb.database.DAOFactory#buildLanguagesProblemsDAO()
     */
    @Override
    public LanguagesProblemsDAO buildLanguagesProblemsDAO() {
        return new LanguagesProblemsHibernateDAO();
    }

    @Override
    public PDFDAO buildPDFDAO() {
        return new PDFHibernateDAO();
    }
}
