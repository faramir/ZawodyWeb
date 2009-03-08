package pl.umk.mat.zawodyweb.database;

import pl.umk.mat.zawodyweb.database.hibernate.HibernateDAOFactory;

/**
 * Generated at Thu Mar 05 04:19:39 CET 2009
 *
 * @see http://www.hibernate.org/328.html
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 */

public abstract class DAOFactory {

	private static final DAOFactory HIBERNATE = new HibernateDAOFactory();

	public static final DAOFactory DEFAULT = HIBERNATE;
	
    /**
     * Factory method for instantiation of concrete factories.
     */
    public static DAOFactory instance(Class factory) {
        try {
            return (DAOFactory)factory.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException("Couldn't create DAOFactory: " + factory);
        }
    }
	
	public abstract SeriesDAO buildSeriesDAO();
	
	public abstract UsersDAO buildUsersDAO();
	
	public abstract SeriesRolesDAO buildSeriesRolesDAO();
	
	public abstract SubmitsResultsTestsDAO buildSubmitsResultsTestsDAO();
	
	public abstract LanguagesDAO buildLanguagesDAO();
	
	public abstract ContestsRolesDAO buildContestsRolesDAO();
	
	public abstract TestsDAO buildTestsDAO();
	
	public abstract ResultsDAO buildResultsDAO();
	
	public abstract ClassesDAO buildClassesDAO();
	
	public abstract SubmitsDAO buildSubmitsDAO();
	
	public abstract ContestsDAO buildContestsDAO();
	
	public abstract QuestionsDAO buildQuestionsDAO();
	
	public abstract TasksDAO buildTasksDAO();
	
	public abstract LanguagesTasksDAO buildLanguagesTasksDAO();
	
	public abstract UsersRolesDAO buildUsersRolesDAO();
	
	public abstract RolesDAO buildRolesDAO();
	
}
