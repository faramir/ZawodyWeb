package org.zawodyweb.database.hibernate;

import org.zawodyweb.database.DAOFactory;
import org.zawodyweb.database.SeriesDAO;
import org.zawodyweb.database.UsersDAO;
import org.zawodyweb.database.SeriesRolesDAO;
import org.zawodyweb.database.SubmitsResultsTestsDAO;
import org.zawodyweb.database.LanguagesDAO;
import org.zawodyweb.database.ContestsRolesDAO;
import org.zawodyweb.database.TestsDAO;
import org.zawodyweb.database.ResultsDAO;
import org.zawodyweb.database.ClassesDAO;
import org.zawodyweb.database.SubmitsDAO;
import org.zawodyweb.database.ContestsDAO;
import org.zawodyweb.database.QuestionsDAO;
import org.zawodyweb.database.TasksDAO;
import org.zawodyweb.database.LanguagesTasksDAO;
import org.zawodyweb.database.UsersRolesDAO;
import org.zawodyweb.database.RolesDAO;

/**
 * Generated at Thu Mar 05 04:19:39 CET 2009
 *
 * @see http://www.hibernate.org/43.html
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 */
public class HibernateDAOFactory extends DAOFactory {

	/* (non-Javadoc)
	 * @see org.zawodyweb.database.DAOFactory#buildSeriesDAO()
	 */
	@Override
	public SeriesDAO buildSeriesDAO() {
		return new SeriesHibernateDAO();
	}
	
	/* (non-Javadoc)
	 * @see org.zawodyweb.database.DAOFactory#buildUsersDAO()
	 */
	@Override
	public UsersDAO buildUsersDAO() {
		return new UsersHibernateDAO();
	}
	
	/* (non-Javadoc)
	 * @see org.zawodyweb.database.DAOFactory#buildSeriesRolesDAO()
	 */
	@Override
	public SeriesRolesDAO buildSeriesRolesDAO() {
		return new SeriesRolesHibernateDAO();
	}
	
	/* (non-Javadoc)
	 * @see org.zawodyweb.database.DAOFactory#buildSubmitsResultsTestsDAO()
	 */
	@Override
	public SubmitsResultsTestsDAO buildSubmitsResultsTestsDAO() {
		return new SubmitsResultsTestsHibernateDAO();
	}
	
	/* (non-Javadoc)
	 * @see org.zawodyweb.database.DAOFactory#buildLanguagesDAO()
	 */
	@Override
	public LanguagesDAO buildLanguagesDAO() {
		return new LanguagesHibernateDAO();
	}
	
	/* (non-Javadoc)
	 * @see org.zawodyweb.database.DAOFactory#buildContestsRolesDAO()
	 */
	@Override
	public ContestsRolesDAO buildContestsRolesDAO() {
		return new ContestsRolesHibernateDAO();
	}
	
	/* (non-Javadoc)
	 * @see org.zawodyweb.database.DAOFactory#buildTestsDAO()
	 */
	@Override
	public TestsDAO buildTestsDAO() {
		return new TestsHibernateDAO();
	}
	
	/* (non-Javadoc)
	 * @see org.zawodyweb.database.DAOFactory#buildResultsDAO()
	 */
	@Override
	public ResultsDAO buildResultsDAO() {
		return new ResultsHibernateDAO();
	}
	
	/* (non-Javadoc)
	 * @see org.zawodyweb.database.DAOFactory#buildClassesDAO()
	 */
	@Override
	public ClassesDAO buildClassesDAO() {
		return new ClassesHibernateDAO();
	}
	
	/* (non-Javadoc)
	 * @see org.zawodyweb.database.DAOFactory#buildSubmitsDAO()
	 */
	@Override
	public SubmitsDAO buildSubmitsDAO() {
		return new SubmitsHibernateDAO();
	}
	
	/* (non-Javadoc)
	 * @see org.zawodyweb.database.DAOFactory#buildContestsDAO()
	 */
	@Override
	public ContestsDAO buildContestsDAO() {
		return new ContestsHibernateDAO();
	}
	
	/* (non-Javadoc)
	 * @see org.zawodyweb.database.DAOFactory#buildQuestionsDAO()
	 */
	@Override
	public QuestionsDAO buildQuestionsDAO() {
		return new QuestionsHibernateDAO();
	}
	
	/* (non-Javadoc)
	 * @see org.zawodyweb.database.DAOFactory#buildTasksDAO()
	 */
	@Override
	public TasksDAO buildTasksDAO() {
		return new TasksHibernateDAO();
	}
	
	/* (non-Javadoc)
	 * @see org.zawodyweb.database.DAOFactory#buildLanguagesTasksDAO()
	 */
	@Override
	public LanguagesTasksDAO buildLanguagesTasksDAO() {
		return new LanguagesTasksHibernateDAO();
	}
	
	/* (non-Javadoc)
	 * @see org.zawodyweb.database.DAOFactory#buildUsersRolesDAO()
	 */
	@Override
	public UsersRolesDAO buildUsersRolesDAO() {
		return new UsersRolesHibernateDAO();
	}
	
	/* (non-Javadoc)
	 * @see org.zawodyweb.database.DAOFactory#buildRolesDAO()
	 */
	@Override
	public RolesDAO buildRolesDAO() {
		return new RolesHibernateDAO();
	}
	
}
