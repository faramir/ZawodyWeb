package org.zawodyweb.database.hibernate;

import java.util.List;
import java.sql.Timestamp;

import org.zawodyweb.database.pojo.LanguagesTasks;
import org.zawodyweb.database.LanguagesTasksDAO;

import org.hibernate.criterion.Restrictions;

/**
 * <p>Hibernate DAO layer for LanguagesTaskss</p>
 * <p>Generated at Thu Mar 05 04:19:39 CET 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @see http://www.hibernate.org/328.html
 */
public class LanguagesTasksHibernateDAO extends
		AbstractHibernateDAO<LanguagesTasks, Integer> implements
		LanguagesTasksDAO {

	/**
	 * Find LanguagesTasks by tasksid
	 */
	@SuppressWarnings("unchecked")
	public List<LanguagesTasks> findByTasksid(Integer tasksid) {
		return findByCriteria(Restrictions.eq("tasks.tasksid", tasksid));
	}
	
	/**
	 * Find LanguagesTasks by languagesid
	 */
	@SuppressWarnings("unchecked")
	public List<LanguagesTasks> findByLanguagesid(Integer languagesid) {
		return findByCriteria(Restrictions.eq("languages.languagesid", languagesid));
	}
	

}
