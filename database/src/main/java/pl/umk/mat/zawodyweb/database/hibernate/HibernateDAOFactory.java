package pl.umk.mat.zawodyweb.database.hibernate;

import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.SeriesDAO;
import pl.umk.mat.zawodyweb.database.UsersDAO;
import pl.umk.mat.zawodyweb.database.SeriesRolesDAO;
import pl.umk.mat.zawodyweb.database.SubmitsResultsTestsDAO;
import pl.umk.mat.zawodyweb.database.LanguagesDAO;
import pl.umk.mat.zawodyweb.database.ContestsRolesDAO;
import pl.umk.mat.zawodyweb.database.TestsDAO;
import pl.umk.mat.zawodyweb.database.ResultsDAO;
import pl.umk.mat.zawodyweb.database.ClassesDAO;
import pl.umk.mat.zawodyweb.database.SubmitsDAO;
import pl.umk.mat.zawodyweb.database.ContestsDAO;
import pl.umk.mat.zawodyweb.database.QuestionsDAO;
import pl.umk.mat.zawodyweb.database.TasksDAO;
import pl.umk.mat.zawodyweb.database.LanguagesTasksDAO;
import pl.umk.mat.zawodyweb.database.UsersRolesDAO;
import pl.umk.mat.zawodyweb.database.RolesDAO;

/**
 * Generated at Thu Mar 05 04:19:39 CET 2009
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
	public UsersDAO buildUsersDAO() {
		return new UsersHibernateDAO();
	}
	
	/* (non-Javadoc)
	 * @see pl.umk.mat.zawodyweb.database.DAOFactory#buildSeriesRolesDAO()
	 */
	@Override
	public SeriesRolesDAO buildSeriesRolesDAO() {
		return new SeriesRolesHibernateDAO();
	}
	
	/* (non-Javadoc)
	 * @see pl.umk.mat.zawodyweb.database.DAOFactory#buildSubmitsResultsTestsDAO()
	 */
	@Override
	public SubmitsResultsTestsDAO buildSubmitsResultsTestsDAO() {
		return new SubmitsResultsTestsHibernateDAO();
	}
	
	/* (non-Javadoc)
	 * @see pl.umk.mat.zawodyweb.database.DAOFactory#buildLanguagesDAO()
	 */
	@Override
	public LanguagesDAO buildLanguagesDAO() {
		return new LanguagesHibernateDAO();
	}
	
	/* (non-Javadoc)
	 * @see pl.umk.mat.zawodyweb.database.DAOFactory#buildContestsRolesDAO()
	 */
	@Override
	public ContestsRolesDAO buildContestsRolesDAO() {
		return new ContestsRolesHibernateDAO();
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
	 * @see pl.umk.mat.zawodyweb.database.DAOFactory#buildQuestionsDAO()
	 */
	@Override
	public QuestionsDAO buildQuestionsDAO() {
		return new QuestionsHibernateDAO();
	}
	
	/* (non-Javadoc)
	 * @see pl.umk.mat.zawodyweb.database.DAOFactory#buildTasksDAO()
	 */
	@Override
	public TasksDAO buildTasksDAO() {
		return new TasksHibernateDAO();
	}
	
	/* (non-Javadoc)
	 * @see pl.umk.mat.zawodyweb.database.DAOFactory#buildLanguagesTasksDAO()
	 */
	@Override
	public LanguagesTasksDAO buildLanguagesTasksDAO() {
		return new LanguagesTasksHibernateDAO();
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
	
}
