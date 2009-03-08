package pl.umk.mat.zawodyweb.database;

import java.util.List;
import java.sql.Timestamp;

import pl.umk.mat.zawodyweb.database.pojo.LanguagesTasks;
/**
 * <p>Generic DAO layer for LanguagesTaskss</p>
 * <p>Generated at Thu Mar 05 04:19:39 CET 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @see http://www.hibernate.org/328.html
 */
public interface LanguagesTasksDAO extends GenericDAO<LanguagesTasks,Integer> {

	/*
	 * TODO : Add specific businesses daos here.
	 * These methods will be overwrited if you re-generate this interface.
	 * You might want to extend this interface and to change the dao factory to return 
	 * an instance of the new implemenation in buildLanguagesTasksDAO()
	 */
	  	 
	/**
	 * Find LanguagesTasks by tasksid
	 */
	public List<LanguagesTasks> findByTasksid(Integer tasksid);

	/**
	 * Find LanguagesTasks by languagesid
	 */
	public List<LanguagesTasks> findByLanguagesid(Integer languagesid);

}